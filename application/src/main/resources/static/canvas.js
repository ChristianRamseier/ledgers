// Initial state of the canvas
let scale, panOffsetX, panOffsetY;

const ZOOM_SPEED = 0.1;
const minScale = 0.35;
const maxScale = 1.25;

let isDragging = false;
let isSpacePressed = false;
let isPanning = false;
let isLinking = false;

let startX = 0;
let startY = 0;
let lastTouchX = 0;
let lastTouchY = 0;
let touchStartPanX = 0;
let touchStartPanY = 0;

let selectedElement, startAnchor, endAnchor

function nodeMoved() {
    console.log('This function should never be called and be implemented elsewhere')
}

function linkCreated() {
    console.log('This function should never be called and be implemented elsewhere')
}

function adjustCanvasToViewport() {
    const nodes = document.querySelectorAll('.node');
    if (nodes.length) {
        let minX = Infinity, maxX = -Infinity, minY = Infinity, maxY = -Infinity;

        nodes.forEach(node => {
            const x = parseInt(node.style.left, 10);
            const y = parseInt(node.style.top, 10);
            const width = node.offsetWidth;
            const height = node.offsetHeight;

            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x + width);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y + height);
        });

        const boundingBoxWidth = maxX - minX;
        const boundingBoxHeight = maxY - minY;
        const viewportWidth = window.innerWidth;
        const viewportHeight = window.innerHeight;

        const scaleX = viewportWidth / (boundingBoxWidth + 80);
        const scaleY = viewportHeight / (boundingBoxHeight + 80);

        scale = Math.min(scaleX, scaleY, 1); // Ensure the scale is not more than 1
        panOffsetX = (viewportWidth - boundingBoxWidth * scale) / 2 - minX * scale;
        panOffsetY = (viewportHeight - boundingBoxHeight * scale) / 2 - minY * scale;
    } else {
        scale = 1
        panOffsetX = 0
        panOffsetY = 0
    }
    // Apply the calculated scale and pan offsets
    applyPanAndZoom();

    document.getElementById('canvas-nodes').style.opacity = 1;
    document.getElementById('canvas-edges-svg').style.opacity = 1;
}

document.addEventListener('DOMContentLoaded', adjustCanvasToViewport);

// Zoom
window.addEventListener('wheel', (e) => {
    if (e.ctrlKey || e.metaKey) {
        if (e.deltaY > 0) {
            scale = Math.max(scale - ZOOM_SPEED, minScale);
        } else {
            scale = Math.min(scale + ZOOM_SPEED, maxScale);
        }

        document.body.style.setProperty('--scale', scale);
        e.preventDefault();
    }
}, {passive: false});

// Buttons
document.getElementById('zoom-in').addEventListener('click', function () {
    scale = Math.min(scale + ZOOM_SPEED, maxScale);
    document.body.style.setProperty('--scale', scale);
});

document.getElementById('zoom-out').addEventListener('click', function () {
    scale = Math.max(scale - ZOOM_SPEED, minScale);
    document.body.style.setProperty('--scale', scale);
});

document.getElementById('zoom-reset').addEventListener('click', function () {
    adjustCanvasToViewport();
});

document.addEventListener('DOMContentLoaded', function () {
    const links = document.querySelectorAll('a');
    links.forEach(link => {
        const url = new URL(link.href);
        if (url.hostname !== window.location.hostname) {
            link.target = '_blank';
            link.rel = 'noopener noreferrer';
        }
    });
});

function getEdges() {
    const edges = [...document.getElementsByTagName('edge')].map(edge => {
        const edgeObject = {
            id: edge.id,
            fromNode: edge.getAttribute('data-from-node'),
            fromSide: edge.getAttribute('data-from-side'),
            toNode: edge.getAttribute('data-to-node'),
            toSide: edge.getAttribute('data-to-side'),
        }
        return edgeObject
    })
    return edges
}

function getAnchorPoint(node, side) {
    const x = parseInt(node.style.left, 10);
    const y = parseInt(node.style.top, 10);
    const width = node.offsetWidth;
    const height = node.offsetHeight;

    switch (side.toLowerCase()) {
        case 'top':
            return {x: x + width / 2, y: y};
        case 'right':
            return {x: x + width, y: y + height / 2};
        case 'bottom':
            return {x: x + width / 2, y: y + height};
        case 'left':
            return {x: x, y: y + height / 2};
        default: // center or unspecified case
            return {x: x + width / 2, y: y + height / 2};
    }
}

function drawEdges() {

    const edges = getEdges()

    const svgContainer = document.getElementById('canvas-edge-paths');
    [...svgContainer.children].forEach(path => {
        const edge = edges.find(e => path.id === `${e.id}.path`)
        if (!edge) {
            svgContainer.removeChild(path)
        }
    })
    edges.forEach(edge => {
        const fromNode = document.getElementById(edge.fromNode);
        const toNode = document.getElementById(edge.toNode);
        if (fromNode && toNode) {
            const fromPoint = getAnchorPoint(fromNode, edge.fromSide);
            const toPoint = getAnchorPoint(toNode, edge.toSide);

            const curveTightness = 0.75;
            const controlPointX1 = fromPoint.x + (toPoint.x - fromPoint.x) * curveTightness;
            const controlPointX2 = fromPoint.x + (toPoint.x - fromPoint.x) * (1 - curveTightness);
            const controlPointY1 = fromPoint.y;
            const controlPointY2 = toPoint.y;

            const d = `M ${fromPoint.x} ${fromPoint.y} C ${controlPointX1} ${controlPointY1}, ${controlPointX2} ${controlPointY2}, ${toPoint.x} ${toPoint.y}`;
            const pathId = `${edge.id}.path`;
            const existingPath = document.getElementById(pathId)
            if (existingPath) {
                existingPath.setAttribute('d', d)
            } else {
                const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
                path.id = pathId
                path.setAttribute('d', d);
                path.setAttribute('stroke', 'black');
                path.setAttribute('fill', 'none');
                path.setAttribute('marker-end', 'url(#arrowhead)');
                svgContainer.appendChild(path);
            }
        }
    });
}

// Drag nodes
function enableDraggingFor(element) {
    element.addEventListener('mousedown', function (e) {
        if (isSpacePressed) return;
        isDragging = true;
        startX = e.clientX;
        startY = e.clientY;
        selectedElement = this.parentElement;
        selectedElement.classList.add('is-dragging');
        document.body.classList.add('is-dragging');
    });
}

function enableAnchorFor(anchorElement) {
    anchorElement.addEventListener('mousedown', function (e) {
        if (isSpacePressed) return;
        isLinking = true
        startAnchor = anchorElement
        startAnchor.classList.add('is-linking')
        startAnchor.parentElement.classList.add('is-linking')
    })
}

document.querySelectorAll('.node .node-name').forEach(nodeName => {
    enableDraggingFor(nodeName)
});

window.addEventListener('mousemove', function (e) {
    if (isDragging && selectedElement) {

        const dx = (e.clientX - startX) / scale;
        const dy = (e.clientY - startY) / scale;

        selectedElement.style.left = `${parseInt(selectedElement.style.left, 10) + dx}px`;
        selectedElement.style.top = `${parseInt(selectedElement.style.top, 10) + dy}px`;

        startX = e.clientX;
        startY = e.clientY;

        drawEdges();
    }
});

window.addEventListener('mousemove', function (e) {
    if (isLinking && startAnchor) {
        drawLinkHintAndDetectEndAnchor(e.clientX, e.clientY)
    }
});

function endDragging() {
    if (isDragging && selectedElement) {
        selectedElement.classList.remove('is-dragging');
        document.body.classList.remove('is-dragging');
        isDragging = false;
        nodeMoved(selectedElement)
        selectedElement = null;
        drawEdges();
    }
}

function endLinking() {
    if (isLinking && startAnchor) {
        startAnchor.classList.remove('is-linking')
        startAnchor.parentElement.classList.remove('is-linking')
        isLinking = false;
        if (endAnchor) {
            linkCreated(startAnchor, endAnchor)
            endAnchor = null
        }
        startAnchor = null
        hideLinkHint()
    }
}

function hideLinkHint() {
    const linkHintContainer = document.getElementById('canvas-edge-link-hint')
    linkHintContainer.innerHTML = ''
}

function drawLinkHintAndDetectEndAnchor(mouseX, mouseY) {

    if (startAnchor) {
        const linkHintContainer = document.getElementById('canvas-edge-link-hint')
        linkHintContainer.innerHTML = ''
        const rect = startAnchor.getBoundingClientRect()
        const startX = rect.x + rect.width / 2;
        const startY = rect.y + rect.height / 2;
        const d = `M ${startX - panOffsetX} ${startY - panOffsetY} ${mouseX - panOffsetX} ${mouseY - panOffsetY}`;
        const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
        path.setAttribute('d', d);
        path.setAttribute('stroke-dasharray', '2,2');
        path.setAttribute('fill', 'none');
        linkHintContainer.appendChild(path);
    }

    const anchorElements = [...document.getElementsByTagName('anchor')];
    endAnchor = null
    anchorElements.forEach(anchor => {
        const rect = anchor.getBoundingClientRect()
        if (mouseX >= rect.left && mouseX <= rect.right && mouseY >= rect.top && mouseY <= rect.bottom) {
            endAnchor = anchor
        }
    })

}

window.addEventListener('mouseup', function (e) {
    endDragging()
    endLinking()
});

// Panning
window.addEventListener('keydown', function (e) {
    if (e.code === 'Space') {
        // e.preventDefault(); - removed since it prevents entry of the space key in text fields
        isSpacePressed = true;
        document.body.classList.add('will-pan');
    }
});

window.addEventListener('keyup', function (e) {
    if (e.code === 'Space') {
        isSpacePressed = false;
        document.body.classList.remove('will-pan');
    }
});

window.addEventListener('mousedown', function (e) {
    if (isSpacePressed && !isDragging) {
        isPanning = true;
        document.body.style.cursor = 'grabbing';
        panStartX = e.clientX - panOffsetX;
        panStartY = e.clientY - panOffsetY;
    }
});

window.addEventListener('mousemove', function (e) {
    if (isPanning) {
        panOffsetX = e.clientX - panStartX;
        panOffsetY = e.clientY - panStartY;

        document.body.style.setProperty('--pan-x', `${panOffsetX}px`);
        document.body.style.setProperty('--pan-y', `${panOffsetY}px`);
    }
});

window.addEventListener('mouseup', function () {
    if (isPanning) {
        isPanning = false;
        document.body.style.cursor = '';
    }
});

// Touch-based devices 
let initialDistance = null;

document.addEventListener('gesturestart', function (e) {
    e.preventDefault();
});

document.getElementById('canvas-container').addEventListener('touchstart', function (e) {
    if (e.touches.length === 1) { // Single touch for panning
        isPanning = true;
        const touch = e.touches[0];
        touchStartPanX = touch.pageX - panOffsetX;
        touchStartPanY = touch.pageY - panOffsetY;
        lastTouchX = touch.pageX;
        lastTouchY = touch.pageY;
    } else if (e.touches.length === 2) { // Two-finger touch for zooming
        e.preventDefault(); // Prevent page zoom
        const touch1 = e.touches[0];
        const touch2 = e.touches[1];
        initialDistance = Math.sqrt((touch2.pageX - touch1.pageX) ** 2 + (touch2.pageY - touch1.pageY) ** 2);
    }
}, {passive: false});

// Touch move for panning and zooming
document.getElementById('canvas-container').addEventListener('touchmove', function (e) {
    if (e.touches.length === 1 && isPanning) {
        const touch = e.touches[0];
        const dx = touch.pageX - lastTouchX;
        const dy = touch.pageY - lastTouchY;
        panOffsetX += dx;
        panOffsetY += dy;
        lastTouchX = touch.pageX;
        lastTouchY = touch.pageY;
        applyPanAndZoom();
        drawEdges();
    } else if (e.touches.length === 2) { // Adjust for zooming
        e.preventDefault();
        const touch1 = e.touches[0];
        const touch2 = e.touches[1];
        const distance = Math.sqrt((touch2.pageX - touch1.pageX) ** 2 + (touch2.pageY - touch1.pageY) ** 2);
        const scaleChange = distance / initialDistance;
        scale = Math.min(Math.max(minScale, scale * scaleChange), maxScale); // Apply and limit scale
        document.body.style.setProperty('--scale', scale);
        initialDistance = distance;
        applyPanAndZoom();
    }
}, {passive: false});

document.getElementById('canvas-container').addEventListener('touchend', function (e) {
    if (isPanning) {
        isPanning = false;
    }
    if (e.touches.length < 2) {
        initialDistance = null; // Reset zoom tracking on lifting one finger
    }
});

// Activate node on touch
document.querySelectorAll('.node .node-name').forEach(nodeName => {
    nodeName.addEventListener('touchstart', function (e) {
        // Prevent activating multiple nodes simultaneously
        deactivateAllNodes();
        const node = this.parentElement;
        node.classList.add('is-active');
        // Prepare for potential drag
        isDragging = false;
        const touch = e.touches[0];
        startX = touch.pageX;
        startY = touch.pageY;
        selectedElement = node;
        e.stopPropagation();
    }, {passive: true});
});

// Deactivate nodes when tapping outside
document.addEventListener('touchstart', function (e) {
    if (!e.target.closest('.node')) {
        deactivateAllNodes();
    }
});

function deactivateAllNodes() {
    document.querySelectorAll('.node').forEach(node => {
        node.classList.remove('is-active');
    });
}

// Handling dragging for an activated node
document.addEventListener('touchmove', function (e) {
    if (isDragging && selectedElement && selectedElement.classList.contains('is-active')) {
        const touch = e.touches[0];
        const dx = (touch.pageX - startX) / scale;
        const dy = (touch.pageY - startY) / scale;
        selectedElement.style.left = `${parseInt(selectedElement.style.left, 10) + dx}px`;
        selectedElement.style.top = `${parseInt(selectedElement.style.top, 10) + dy}px`;

        // Update startX and startY for the next move event
        startX = touch.pageX;
        startY = touch.pageY;

        // Call drawEdges to update edge positions based on the new node positions
        drawEdges();

        e.preventDefault(); // Prevent default to avoid scrolling and other touch actions
    }
}, {passive: false});

// Determine if dragging should start
document.addEventListener('touchmove', function (e) {
    if (selectedElement && !isDragging) {
        const touch = e.touches[0];
        if (Math.abs(touch.pageX - startX) > 10 || Math.abs(touch.pageY - startY) > 10) {
            isDragging = true; // Start dragging if moved beyond threshold
        }
    }
}, {passive: true});

// Check if hovering over anchor
document.addEventListener('touchmove', function (e) {
    if (isLinking) {
        drawLinkHintAndDetectEndAnchor(e.touches[0].clientX, e.touches[0].clientY)
    }
}, {passive: true});

// End dragging
document.addEventListener('touchend', function () {
    endDragging()
    endLinking()
});


function applyPanAndZoom() {
    document.body.style.setProperty('--scale', scale);
    document.body.style.setProperty('--pan-x', `${panOffsetX}px`);
    document.body.style.setProperty('--pan-y', `${panOffsetY}px`);
}

// Prevent the whole page from zooming on pinch
document.addEventListener('gesturestart', function (e) {
    e.preventDefault();
});

document.addEventListener('gesturechange', function (e) {
    e.preventDefault();
});

window.addEventListener('dragstart', function (e) {
    e.preventDefault();
});

drawEdges();

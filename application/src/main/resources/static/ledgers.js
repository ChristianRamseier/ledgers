// the display state after the last transition, i.e. the starting point for new transitions
let state = {
    story: undefined,
    chapter: 0,
    step: 0
}

let storyInputs = {}

function loadStory() {
    const storyElement = document.getElementById('story-element')
    storyElement.addEventListener('storyUpdated', (e) => {
        const update = e.detail
        const story = update.story
        const chapter = update.chapter
        const step = update.step
        transition(state.story, state.chapter, story, chapter)
        state = {
            story: story,
            chapter: chapter,
            step: step,
        }
    })
    storyElement.addEventListener('storyInitialized', (e) => {
        // Save event emitter in order to send events to the component later
        const initializedEvent = e.detail
        storyInputs = initializedEvent.eventEmitter
        state.story = initializedEvent.emptyStory
    });
}

const transition = function (fromStory, fromChapter, toStory, toChapter) {
    const stageChanges = fromStory.getStageChanges(fromChapter, toStory, toChapter)
    const changes = stageChanges.changes
    const newStage = stageChanges.newStage
    changes.forEach(change => {
        const component = toStory.getComponent(change.componentReference)
        switch (component.type.name) {
            case 'Ledger': {
                changeNodesOnStage(change, component)
                break;
            }
            case 'Link': {
                changeLinkOnStage(change, component, newStage)
                break;
            }
        }
    })
    drawEdges()
}

function changeLinkOnStage(change, component, newStage) {
    switch (change.type.name) {

        case 'Add': {
            const edges = document.getElementById('canvas-edges')
            const newEdge = document.createElement('edge');
            newEdge.id = `${component.reference}`
            newEdge.setAttribute('data-from-node', `${newStage.getComponentOnStageById(component.from).reference}`)
            newEdge.setAttribute('data-to-node', `${newStage.getComponentOnStageById(component.to).reference}`)
            newEdge.setAttribute('data-from-side', `${change.component.fromAnchor}`)
            newEdge.setAttribute('data-to-side', `${change.component.toAnchor}`)
            edges.appendChild(newEdge)
            break;
        }

        case 'Change': {
            const edge = document.getElementById(`${component.reference}`)
            edge.setAttribute('data-from-node', `${newStage.getComponentOnStageById(component.from).reference}`)
            edge.setAttribute('data-to-node', `${newStage.getComponentOnStageById(component.to).reference}`)
            edge.setAttribute('data-from-side', `${change.component.fromAnchor}`)
            edge.setAttribute('data-to-side', `${change.component.toAnchor}`)
            break;
        }

        case 'Remove': {
            const edges = document.getElementById('canvas-edges')
            const edge = document.getElementById(`${component.reference}`)
            edges.removeChild(edge)
            break;
        }

    }
}

function createAnchor(component, anchor) {
    const anchorElement = document.createElement('anchor')
    anchorElement.id = `${component.reference}.anchor.${anchor}`
    anchorElement.className = `node-anchor-${anchor}`
    return anchorElement
}

function changeNodesOnStage(change, component) {
    switch (change.type.name) {

        case 'Add': {
            const nodes = document.getElementById('canvas-nodes')
            const newNode = document.createElement('node');
            newNode.id = `${component.reference}`
            newNode.className = 'node node-link'
            newNode.style.left = `${change.component.box.x}px`
            newNode.style.top = `${change.component.box.y}px`
            newNode.style.width = `${change.component.box.width}px`
            newNode.style.height = `${change.component.box.height}px`
            const name = document.createElement('div')
            name.id = `${component.reference}.name`
            name.className = 'node-name'
            name.textContent = component.name
            newNode.appendChild(name)
            const topAnchor = createAnchor(component, 'top');
            newNode.appendChild(topAnchor)
            enableAnchorFor(topAnchor)
            const leftAnchor = createAnchor(component, 'left');
            newNode.appendChild(leftAnchor)
            enableAnchorFor(leftAnchor)
            const rightAnchor = createAnchor(component, 'right');
            newNode.appendChild(rightAnchor)
            enableAnchorFor(rightAnchor)
            const bottomAnchor = createAnchor(component, 'bottom');
            newNode.appendChild(bottomAnchor)
            enableAnchorFor(bottomAnchor)
            nodes.appendChild(newNode)
            enableDraggingFor(name)
            break;
        }

        case 'Change': {
            const node = document.getElementById(`${component.reference}`)
            node.style.left = `${change.component.box.x}px`
            node.style.top = `${change.component.box.y}px`
            node.style.width = `${change.component.box.width}px`
            node.style.height = `${change.component.box.height}px`
            const name = document.getElementById(`${component.reference}.name`)
            name.textContent = component.name
            break;
        }

        case 'Remove': {
            const nodes = document.getElementById('canvas-nodes')
            const node = document.getElementById(`${component.reference}`)
            nodes.removeChild(node)
            break;
        }

    }

}

/*
        document.getElementById(`component:${l.reference}`).addEventListener('dblclick', function (event) {
            const width = 200
            const height = 200
            const x = (window.innerWidth / 2 - panOffsetX - width / 2) * scale;
            const y = (window.innerHeight / 2 - panOffsetY - height / 2) * scale;
            const box = new domain.stage.Box(x, y, width, height);
            const story = state.story.withLedgerInChapter(state.chapter, l.reference, box)
            transition(state.story, state.chapter, story, state.chapter)
            updateStory(story)
        })
*/


function nodeMoved(node) {
    const width = 200
    const height = 200
    const x = parseInt(node.style.left, 10);
    const y = parseInt(node.style.top, 10);
    storyInputs.emit({
        type: 'move-ledger',
        x: x,
        y: y,
        width: width,
        height: height,
        reference: node.id
    })

}

function linkCreated(startAnchor, endAnchor) {
    storyInputs.emit({
        type: 'create-link',
        startAnchor: startAnchor.id,
        endAnchor: endAnchor.id
    })
}

document.addEventListener("DOMContentLoaded", (event) => {
    loadStory()
})

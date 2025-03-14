const domain = window['org.ledgers:core'].org.ledgers.domain

let state = {
    story: domain.Story.Companion.new(),
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
        transition(state.story, state.chapter, story, chapter)
        state = {
            story: story,
            chapter: 0,
            step: 0
        }
    })
    storyElement.addEventListener('storyInitialized', (e) => {
        storyInputs = e.detail
    });
}

function updateStory(story) {
    console.log('legacy update of story', story)
}

const transition = function (fromStory, fromChapter, toStory, toChapter) {
    const currentStage = fromStory.storyline.getStageAtChapter(fromChapter)
    const newStage = toStory.storyline.getStageAtChapter(toChapter)
    const changes = currentStage.getChangesThatLeadTo(newStage)
    changes.toArray().forEach(change => {
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
            newEdge.setAttribute('data-from-node', `${newStage.getById(component.from).reference}`)
            newEdge.setAttribute('data-to-node', `${newStage.getById(component.to).reference}`)
            newEdge.setAttribute('data-from-side', `${change.component.fromAnchor}`)
            newEdge.setAttribute('data-to-side', `${change.component.toAnchor}`)
            edges.appendChild(newEdge)
            break;
        }

        case 'Change': {
            const edge = document.getElementById(`${component.reference}`)
            edge.setAttribute('data-from-node', `${newStage.getById(component.from).reference}`)
            edge.setAttribute('data-to-node', `${newStage.getById(component.to).reference}`)
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
    anchorElement.id = `${component.reference}.${anchor}`
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
    //const fromAnchorReference = domain.stage.AnchorReference.Companion.fromString(startAnchor.id)
    //const toAnchorReference = domain.stage.AnchorReference.Companion.fromString(endAnchor.id)
    //const story = state.story.withLinkInChapter(state.chapter, fromAnchorReference, toAnchorReference)
    transition(state.story, state.chapter, story, state.chapter)
    updateStory(story)
}


document.addEventListener("DOMContentLoaded", (event) => {
    loadStory()
})

const dto = window['org.ledgers:core'].org.ledgers.dto
const domain = window['org.ledgers:core'].org.ledgers.domain
const usecase = window['org.ledgers:core'].org.ledgers.usecase

let state = {
    story: domain.Story.Companion.new(),
    chapter: 0,
    step: 0
}

function loadStory() {
    const storyId = document.getElementById('story-id').getAttribute('storyId')
    fetch(`/api/story/${storyId}`).then(response => {
        response.text().then(json => {
            const story = dto.fromJson(json)
            transition(state.story, state.chapter, story, 0)
            state = {
                story: story,
                chapter: 0,
                step: 0
            }
            updateDisplay()
        })
    })
}

function updateStory(story) {
    state.story = story
    fetch(`/api/story/${story.id}`, {
        method: 'POST',
        body: dto.toJson(story),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
    })
}

function updateDisplay() {
    updateComponentList()
    updateChaptersList()
    updateChapterDisplay()
    toggleEditorDisplay()
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
    updateChapterDisplay()
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
    anchorElement.id = `${new domain.stage.AnchorReference(component.reference, domain.stage.Anchor.Companion.fromString(anchor))}`
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


const updateComponentList = function () {
    const organizations = state.story.architecture.organizations.toArray()
    const ledgers = state.story.architecture.ledgers.toArray()
    const assets = state.story.architecture.assets.toArray()
    const organizationsHtml = organizations.map(o =>
        `<organization id="component:${o.reference}">${o.name}</organization>`
    ).join('\n')
    const ledgersHtml = ledgers.map(l =>
        `<ledger id="component:${l.reference}">${l.name}</ledger>`
    ).join('\n')
    const assetsHtml = assets.map(a =>
        `<asset id="component:${a.reference}">${a.name}</asset>`
    ).join('\n')
    document.getElementById('components-list').innerHTML = organizationsHtml + ledgersHtml + assetsHtml
    organizations.forEach(o => {
        document.getElementById(`component:${o.reference}`).addEventListener('click', function (event) {
            editOrganization(o)
        })
    })

    assets.forEach(a => {
        document.getElementById(`component:${a.reference}`).addEventListener('click', function (event) {
            editAsset(a)
        })
    })

    ledgers.forEach(l => {
        document.getElementById(`component:${l.reference}`).addEventListener('click', function (event) {
            editLedger(l)
        })
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
    })
}

const updateChaptersList = function () {
    const chapters = state.story.storyline.chapters.toArray()
    const html = chapters.map((chapter, i) =>
        `<chapter id="chapter-${i}">${(i + 1)}</chapter>`
    ).join('\n')
    document.getElementById('chapters-list').innerHTML = html
    chapters.forEach((chapter, i) => {
            document.getElementById(`chapter-${i}`).addEventListener('click', function (event) {
                transition(state.story, state.chapter, state.story, i)
                state.chapter = i
                state.step = 0
                updateChapterDisplay()
            });
        }
    )
}


document.getElementById('add-organization').addEventListener('click', function () {
    const story = state.story.addOrganization(`Organization #${state.story.architecture.organizations.numberOfOrganizations + 1}`);
    updateStory(story)
    editOrganization(state.story.architecture.organizations.last)
    updateComponentList()
});

document.getElementById('add-asset').addEventListener('click', function () {
    const story = state.story.addAsset(`Asset #${state.story.architecture.assets.numberOfAssets + 1}`);
    updateStory(story)
    editAsset(state.story.architecture.assets.last)
    updateComponentList()
});

document.getElementById('add-ledger').addEventListener('click', function () {
    const organizations = state.story.architecture.organizations
    if (organizations.numberOfOrganizations > 0) {
        const story = state.story.addLedger(`Ledger #${state.story.architecture.ledgers.numberOfLedgers + 1}`, organizations.last.id);
        updateStory(story)
        editLedger(state.story.architecture.ledgers.last)
        updateComponentList()
    } else {
        alert('Please add an organization first.')
    }
});

document.getElementById('add-chapter').addEventListener('click', function () {
    const numberOfChapters = state.story.storyline.numberOfChapters;
    const story = state.story.withChapterNamed(numberOfChapters, '')
    updateStory(story)
    updateChaptersList()
    updateChapterDisplay()
})

document.getElementById('add-chapter-scenario-step').addEventListener('click', function () {
    const story = state.story.withNewScenarioStep(state.chapter)
    console.log(story)
    updateStory(story)
    updateChaptersList()
    updateChapterDisplay()
})

let editorState;
resetEditors()

function resetEditors() {
    editorState = {editor: undefined, edited: undefined}
    toggleEditorDisplay()
}

function editLedger(ledger) {
    editorState.editor = 'ledger'
    editorState.edited = ledger
    toggleEditorDisplay()
    const organization = state.story.architecture.organizations.toArray()
    const organizationsHtml = organization.map(o =>
        `<option value="${o.id}">${o.name}</option>`
    ).join('\n')
    document.getElementById('ledger-name').value = ledger.name
    const ledgerOrganization = document.getElementById('ledger-organization');
    ledgerOrganization.innerHTML = organizationsHtml
    ledgerOrganization.value = ledger.ownerId
}

function editOrganization(organization) {
    editorState.editor = 'organization'
    editorState.edited = organization
    toggleEditorDisplay()
    document.getElementById('organization-name').value = organization.name
}

function editAsset(asset) {
    editorState.editor = 'asset'
    editorState.edited = asset
    toggleEditorDisplay()
    document.getElementById('asset-name').value = asset.name
    document.getElementById('asset-type-cash').checked = (asset.assetType.name === 'Cash')
    document.getElementById('asset-type-security').checked = (asset.assetType.name === 'Security')
}

function flexIf(condition) {
    if (condition) {
        return 'display: flex'
    }
    return 'display: none'
}

function updateChapterDisplay() {
    // Chapter Name
    const chapter = state.story.storyline.atChapter(state.chapter)
    const chapterNameEditable = document.getElementById('chapter-name')
    chapterNameEditable.setAttribute('label', `Chapter ${state.chapter + 1}${chapter.name ? ' -' : ''}`)
    chapterNameEditable.setAttribute('value', chapter.name || '')

    // Chapter Changes
    const changes = chapter.getChangesAsArray()
    const changeHtml = changes.map(c => {
        const name = state.story.getComponentDisplayName(c.componentReference, state.chapter)
        return `<stage-change id="stage-change:${c.componentReference}">${c.type} ${c.componentReference.type} ${name}</stage-change>`
    }).join('\n')
    document.getElementById('chapter-changes').innerHTML = changeHtml
    changes.forEach(c => {
        document.getElementById(`stage-change:${c.componentReference}`).addEventListener('click', function (event) {
            // remove?
        })
    })

    // Chapter Scenario
    const hasScenario = !chapter.scenario.isEmpty();
    document.getElementById('chapter-scenario-step-name').style = flexIf(hasScenario)
    if (hasScenario) {
        // Step Name
        const step = chapter.scenario.atStep(state.step)
        const stepNameEditable = document.getElementById('chapter-scenario-step-name')
        stepNameEditable.setAttribute('label', `Step ${state.step + 1}${step.description ? ' -' : ''}`)
        stepNameEditable.setAttribute('value', step.description || '')
    }
    // Step List
    const steps = chapter.scenario.getStepsAsArray()
    const stepsHtml = steps.map((step, index) => {
        return `<step id="step:${index}">${index + 1}</step>`
    }).join('\n')
    document.getElementById('chapter-scenario-steps').innerHTML = stepsHtml
    steps.forEach((step, index) => {
        document.getElementById(`step:${index}`).addEventListener('click', function (event) {
            state.step = index
            updateChapterDisplay()
        })
    })

}

function toggleEditorDisplay() {
    document.getElementById('ledger-editor').style = flexIf(editorState.editor === 'ledger')
    document.getElementById('organization-editor').style = flexIf(editorState.editor === 'organization')
    document.getElementById('asset-editor').style = flexIf(editorState.editor === 'asset')
}

function nodeMoved(node) {
    const width = 200
    const height = 200
    const x = parseInt(node.style.left, 10);
    const y = parseInt(node.style.top, 10);
    const box = new domain.stage.Box(x, y, width, height);
    const reference = domain.component.ComponentReference.Companion.fromString(node.id);
    const story = state.story.withLedgerInChapter(state.chapter, reference, box)
    updateStory(story)
    updateChapterDisplay()
}

function linkCreated(startAnchor, endAnchor) {
    const fromAnchorReference = domain.stage.AnchorReference.Companion.fromString(startAnchor.id)
    const toAnchorReference = domain.stage.AnchorReference.Companion.fromString(endAnchor.id)
    const story = state.story.withLinkInChapter(state.chapter, fromAnchorReference, toAnchorReference)
    transition(state.story, state.chapter, story, state.chapter)
    updateStory(story)
    updateChapterDisplay()
}

document.getElementById('ledger-apply').addEventListener('click', function () {
    if (editorState.editor == 'ledger') {
        const name = document.getElementById('ledger-name').value
        const organization = new domain.architecture.OrganizationId(document.getElementById('ledger-organization').value)
        const reference = editorState.edited.reference
        const story = state.story.withChangedLedger(editorState.edited.reference, name, organization)
        transition(state.story, state.chapter, story, state.chapter)
        updateStory(story)
        const nameOnStage = document.getElementById(`${reference}.name`)
        if (nameOnStage) {
            nameOnStage.textContent = name
        }
        resetEditors()
        updateComponentList()
        updateChapterDisplay()
    }
})

document.getElementById('chapter-scenario-step-name').addEventListener('onChange', function (event) {
    const name = event.detail
    const story = state.story.withDescriptionAtStep(state.chapter, state.step, name)
    updateStory(story)
    updateChapterDisplay()
})

document.getElementById('chapter-name').addEventListener('onChange', function (event) {
    const name = event.detail
    const story = state.story.withChapterNamed(state.chapter, name)
    updateStory(story)
    updateChapterDisplay()
})


document.getElementById('organization-apply').addEventListener('click', function () {
    if (editorState.editor == 'organization') {
        const name = document.getElementById('organization-name').value
        const story = state.story.withChangedOrganization(editorState.edited.reference, name)
        transition(state.story, state.chapter, story, state.chapter)
        updateStory(story)
        resetEditors()
        updateComponentList()
    }
})

document.getElementById('asset-apply').addEventListener('click', function () {
    if (editorState.editor == 'asset') {
        const name = document.getElementById('asset-name').value
        const type = document.getElementById('asset-type-cash').checked ? domain.AssetType.Cash : domain.AssetType.Security
        const story = state.story.withChangedAsset(editorState.edited.reference, name, type)
        transition(state.story, state.chapter, story, state.chapter)
        updateStory(story)
        resetEditors()
        updateComponentList()
    }
})

updateDisplay()
loadStory()

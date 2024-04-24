const domain = window['org.ledgers:core'].org.ledgers.domain
const usecase = window['org.ledgers:core'].org.ledgers.usecase

let state = {
    story: domain.Story.Companion.new(),
    chapter: 0
}

const transition = function (fromStory, fromChapter, toStory, toChapter) {
    const currentStage = fromStory.storyline.getStageAtChapter(fromChapter)
    const newStage = toStory.storyline.getStageAtChapter(toChapter)
    const changes = currentStage.getChangesThatLeadTo(newStage)
    changes.toArray().forEach(change => {
        const component = toStory.getComponent(change.componentReference)
        switch (change.type.name) {

            case 'Add': {
                const nodes = document.getElementById('canvas-nodes')
                const newNode = document.createElement('node');
                newNode.id = `node:${component.reference}`
                newNode.className = 'node node-link'
                newNode.style.left = `${change.component.box.x}px`
                newNode.style.top = `${change.component.box.y}px`
                newNode.style.width = `${change.component.box.width}px`
                newNode.style.height = `${change.component.box.height}px`
                const name = document.createElement('div')
                name.id = `node:${component.reference}:name`
                name.className = 'node-name'
                name.textContent = component.name
                newNode.appendChild(name)
                nodes.appendChild(newNode)
                enableDraggingFor(name)
                break;
            }

            case 'Change': {
                const node = document.getElementById(`node:${component.reference}`)
                node.x = change.component.box.x
                node.y = change.component.box.y
                const name = document.getElementById(`node:${component.reference}:name`)
                name.textContent = change.component.name
                break;
            }

            case 'Remove': {
                const nodes = document.getElementById('canvas-nodes')
                const node = document.getElementById(`node:${component.reference}`)
                nodes.removeChild(node)
                break;
            }

        }
    })
}

const updateComponentList = function () {
    const organizations = state.story.architecture.organizations.toArray()
    const ledgers = state.story.architecture.ledgers.toArray()
    const assets = state.story.architecture.assets.toArray()
    const organizationsHtml = organizations.map(o =>
        `<organization id="${o.reference}">${o.name}</organization>`
    ).join('\n')
    const ledgersHtml = ledgers.map(l =>
        `<ledger id="${l.reference}">${l.name}</ledger>`
    ).join('\n')
    const assetsHtml = assets.map(a =>
        `<asset id="${a.reference}">${a.name}</asset>`
    ).join('\n')
    document.getElementById('components-list').innerHTML = organizationsHtml + ledgersHtml + assetsHtml
    organizations.forEach(o => {
        document.getElementById(`${o.reference}`).addEventListener('click', function (event) {
            editOrganization(o)
        })
    })

    assets.forEach(a => {
        document.getElementById(`${a.reference}`).addEventListener('click', function (event) {
            editAsset(a)
        })
    })

    ledgers.forEach(l => {
        document.getElementById(`${l.reference}`).addEventListener('click', function (event) {
            editLedger(l)
        })
        document.getElementById(`${l.reference}`).addEventListener('dblclick', function (event) {
            const width = 200
            const height = 200
            const x = (window.innerWidth / 2 - panOffsetX - width / 2) * scale;
            const y = (window.innerHeight / 2 - panOffsetY - height / 2) * scale;
            const box = new domain.stage.Box(x, y, width, height);
            const story = state.story.withComponentInChapter(state.chapter, l.reference, box)
            transition(state.story, state.chapter, story, state.chapter)
            state.story = story
        })
    })
}

const updateChaptersList = function () {
    const chapters = state.story.storyline.chapters.toArray()
    const html = chapters.map((o, i) =>
        `<chapter id="chapter-${i}">${o.name || 'Chapter ' + (i + 1)}</chapter>`
    ).join('\n')
    document.getElementById('chapters-list').innerHTML = html
    chapters.forEach((o, i) =>
        document.getElementById(`chapter-${i}`).addEventListener('click', function (event) {
            transition(state.story, state.chapter, state.story, i)
            state.chapter = i
        })
    )
}

document.getElementById('add-organization').addEventListener('click', function () {
    state.story = state.story.addOrganization(`Organization #${state.story.architecture.organizations.numberOfOrganizations + 1}`)
    updateComponentList()
});

document.getElementById('add-asset').addEventListener('click', function () {
    state.story = state.story.addAsset(`Asset #${state.story.architecture.assets.numberOfAssets + 1}`)
    updateComponentList()
});

document.getElementById('add-ledger').addEventListener('click', function () {
    state.story = state.story.addLedger(`Ledger #${state.story.architecture.ledgers.numberOfLedgers + 1}`)
    updateComponentList()
});

document.getElementById('add-chapter').addEventListener('click', function () {
    const numberOfChapters = state.story.storyline.numberOfChapters;
    state.story = state.story.withChapterNamed(numberOfChapters, '')
    updateChaptersList()
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
    document.getElementById('ledger-name').value = ledger.name
    document.getElementById('ledger-organization').value = ledger.organizationId
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

function toggleEditorDisplay() {
    document.getElementById('ledger-editor').style = flexIf(editorState.editor === 'ledger')
    document.getElementById('organization-editor').style = flexIf(editorState.editor === 'organization')
    document.getElementById('asset-editor').style = flexIf(editorState.editor === 'asset')
    document.getElementById('chapter-editor').style = flexIf(editorState.editor === 'chapter')
}

document.getElementById('ledger-apply').addEventListener('click', function () {
    if (editorState.editor == 'ledger') {
        const name = document.getElementById('ledger-name').value
        const organization = document.getElementById('ledger-organization').value
        const reference = editorState.edited.reference
        const story = state.story.withChangedLedger(editorState.edited.reference, name, organization)
        transition(state.story, state.chapter, story, state.chapter)
        state.story = story
        const nameOnStage = document.getElementById(`node:${reference}:name`)
        if (nameOnStage) {
            nameOnStage.textContent = name
        }
        resetEditors()
        updateComponentList()
    }
})

document.getElementById('organization-apply').addEventListener('click', function () {
    if (editorState.editor == 'organization') {
        const name = document.getElementById('organization-name').value
        const story = state.story.withChangedOrganization(editorState.edited.reference, name)
        transition(state.story, state.chapter, story, state.chapter)
        state.story = story
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
        state.story = story
        resetEditors()
        updateComponentList()
    }
})

updateComponentList()
updateChaptersList()
toggleEditorDisplay()
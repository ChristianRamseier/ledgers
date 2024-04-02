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
                newNode.textContent = component.name;
                newNode.id = `node:${component.reference}`
                newNode.x = change.component.location.x
                newNode.y = change.component.location.y
                newNode.width = 100
                newNode.height = 100
                nodes.appendChild(newNode);
                break;
            }

            case 'Change': {
                const node = document.getElementById(`node:${component.reference}`)
                node.x = change.component.location.x
                node.y = change.component.location.y
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
    const html = organizations.map(o =>
        `<organization id="${o.reference}">${o.name}</organization>`
    ).join('\n')
    document.getElementById('components-list').innerHTML = html
    organizations.forEach(o =>
        document.getElementById(`${o.reference}`).addEventListener('click', function (event) {
            const story = state.story.withComponentInChapter(state.chapter, o.reference)
            transition(state.story, state.chapter, story, state.chapter)
            state.story = story
        })
    )
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

document.getElementById('add-chapter').addEventListener('click', function () {
    const numberOfChapters = state.story.storyline.numberOfChapters;
    state.story = state.story.withChapterNamed(numberOfChapters, '')
    updateChaptersList()
})

updateComponentList()
updateChaptersList()

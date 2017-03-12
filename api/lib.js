const fs = require('fs')
const path = require('path')
const uuidV4 = require('uuid/v4');
const sharedIds = require('./state.json')

function loadJsonText(jsonPath) {
    return fs.readFileSync(jsonPath).toString()
}

function loadJson(jsonPath) {
    return require(jsonPath)
}

function loadQuery(name, values) {
    const query = loadJsonText(path.join(__dirname, 'fixtures', `${name}.json`))
    const modified = query
        .replace('$USERNAME', values.username)
        .replace('$AFFILIATION', values.affiliation)
        .replace('$PASSWORD', values.password)
        .replace('$UUID', uuidV4())
        .replace('$PARCELID', sharedIds.parcelId)
        .replace('$ROUTEID', sharedIds.routeId)

    return JSON.parse(modified)
}

function loadQueryText(name, values) {
    return JSON.stringify(loadQuery(name, values))
}

module.exports = {
    loadJsonText,
    loadJson,
    loadQuery,
    loadQueryText
}
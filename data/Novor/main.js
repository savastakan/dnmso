const NovorScorer = require('./NovorScorer')
const { existsSync } = require('fs')

const pipeline = (...fns) => {
    return function (input) {
        return fns.reduce(function (acc, f) {
            return f(acc)
        }, input)
    }
}

const params = {}

const main = (params) => {
    const args = process.argv
    args.splice(0, 2)
    if (args.length < 1) {
        console.log('Use help command for example usage.')
        return process.exit()
    }
    if (args[0] == 'help') {
        console.log('usage:\nnode main.js <mgf_file_path> <novor_result_file_path> <match_result_output_file_path>\n##\nexample:\nnode main.js \'/home/pjskyr/IdeaProjects/dnmso/data/1000spectra.mgf\' \'/home/pjskyr/IdeaProjects/dnmso/data/Novor1-1beta.csv\' \'/home/pjskyr/IdeaProjects/dnmso/data/novor11result.json\'')
        return process.exit()
    }
    if (!existsSync(args[0]) || !existsSync(args[1])) {
        console.log('MGF or CSV files doesn\'t exists.')
        return process.exit()
    }
    params.path = args[0]
    params.path2 = args[1]
    params.path3 = args[2]
    
    return params
}

pipeline(
    main,
    NovorScorer.openFiles,
    NovorScorer.findPeptideInMgf,
    NovorScorer.findPeptideInCsv,
    NovorScorer.findNovorVersionInCsv,
    NovorScorer.match,
    NovorScorer.log,
    NovorScorer.saveFiles
)(params)
const inputFiles = {
    novorinput: '../Novor1-05.csv',
    mgfinput: '../1000spectra.mgf',
}

const outputFile = 'output-files/novor_1_05_match_rate.json'
const outputFile2 = 'output-files/wo_parentheses_novor_1_05_match_rate.json'

const { parse } = require('@fast-csv/parse')

const fs = require('fs')
const stream = require('stream')
const readline = require('readline')

let CSV_STRING = (fs.readFileSync(inputFiles.novorinput, { encoding: 'utf8' })).split('\n\n\n')
const MGF_SCAN_PATTERN = /seq=\{(.*?)\}(.*?)\nSCANS=(.*)/gm

const instream = fs.createReadStream(inputFiles.mgfinput)
const outstream = new stream
const rl = readline.createInterface(instream, outstream)


if (CSV_STRING.length > 1)
    CSV_STRING = CSV_STRING[1]
else
    CSV_STRING = CSV_STRING[0]

CSV_STRING = CSV_STRING.replace(/^\s*$(?:\r\n?|\n)/gm, '')

new Promise(res => {
    let MGF_STRING = ''
    rl.on('line', function (line) {
        MGF_STRING += line + '\n'
    });

    rl.on('close', function () {
        return res(MGF_STRING)
    })
}).then(mgf => {
    let results = {}

    let mgf_data;
    while ((mgf_data = MGF_SCAN_PATTERN.exec(mgf)) !== null)
        results[mgf_data[3]] = mgf_data[1]
    let checker_output = []
    let checker_wo_parentheses_output = []
    const stream = parse({ headers: true })
        .on('error', error => console.error(error))
        .on('data', row => {
            let r = {}
            let rwop = {}
            /* With parantheses */
            r.scanNum = row[' scanNum'].trim()
            r.novorPeptide = row[' peptide'].trim()
            r.expectedPeptide = results[row[' scanNum'].trim()].trim()

            if (results[r.scanNum].trim() == r.novorPeptide)
                r.success = 1
            else
                r.success = 0
            checker_output.push(r)

            /* Without parantheses */
            rwop.scanNum = row[' scanNum'].trim()
            rwop.novorPeptide = row[' peptide'].trim().replace(/\([^)]*\)/g, '')
            rwop.expectedPeptide = results[row[' scanNum'].trim()].trim()

            if (results[rwop.scanNum].trim() == rwop.novorPeptide)
                rwop.success = 1
            else
                rwop.success = 0
            checker_wo_parentheses_output.push(rwop)
        })
        .on('end', () => {
            fs.writeFileSync(outputFile, JSON.stringify(checker_output, null, 4))
            fs.writeFileSync(`${outputFile2}`, JSON.stringify(checker_wo_parentheses_output, null, 4))
            let countOfSuccess = checker_output.filter(row => row.success == 1).length
            let countOfSuccessWOP = checker_wo_parentheses_output.filter(row => row.success == 1).length

            console.log(`Used novor 1.05 version with default config.\nMatch rate is ${countOfSuccess}/${checker_output.length},\nMatch rate without parentheses [(Cam), (O)] is ${countOfSuccessWOP}/${checker_wo_parentheses_output.length}.\nYou should check '${outputFile}' and '${`wo_parentheses_${outputFile}`}' for more info.`)
        }
        )

    stream.write(CSV_STRING)
    stream.end()
})





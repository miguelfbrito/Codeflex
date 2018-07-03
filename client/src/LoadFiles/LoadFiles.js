import React from 'react';
import Files from 'react-files';
import { blobToString } from '../commons/Utils';
import './LoadFiles.css';

class LoadFiles extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            files: []
        }
    }

    onFilesChange = (newFiles) => {
        this.setState({ files: newFiles });
        this.props.addTestCasesFromFiles(newFiles);
    }

    onFilesError = (error, file) => {
        console.log('error code' + error.code + ': ' + error.message);
    }


    render() {
        return (
            <div className="files" style={{ textAlign: 'center' }}>
                <Files
                    className='files-dropzone'
                    onChange={this.onFilesChange}
                    onError={this.onFilesError}
                    accepts={['.txt']}
                    multiple
                    maxFiles={30}
                    maxFileSize={10000000}
                    minFileSize={0}
                    clickable
                >
                    <div style={{ textAlign: 'center' }}>

                        <p>Drop your txt files here or click to upload </p>
                        <p>The format expected for each test case is the following:</p>
                        <p>[test_case_number]_input</p>
                        <p>[test_case_number]_output</p>
                        <p>Maximum of 15 testcases (30 files)</p>
                    </div>
                </Files>

                <div>
                    {this.state.files.map(f => (
                        <div>
                            <p>{f.name}</p>
                        </div>
                    ))}
                </div>
            </div>
        )
    }
}

export default LoadFiles;
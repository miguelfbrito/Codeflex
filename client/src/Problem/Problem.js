import React, { Component } from 'react';
import Script from 'react-load-script';
import AceEditor from 'react-ace';
import brace from 'brace';

import { URL } from '../commons/Constants';
import { splitUrl, textToLowerCaseNoSpaces } from '../commons/Utils';

import 'brace/mode/java';
import 'brace/mode/javascript';
import 'brace/mode/csharp';
import 'brace/mode/c_cpp';
import 'brace/mode/python';

import 'brace/theme/github';

import './Problem.css';

class Problem extends Component {

    constructor(props) {
        super(props);
        this.state = {
            problem: ''
        }

    }

    componentDidMount() {
        let currentProblem = splitUrl(this.props.location.pathname)[2];
        console.log(currentProblem);
        fetch(URL + ':8080/api/database/problem/getProblemByName/' + currentProblem).then(res => res.json()).then(data => { this.setState({ problem: data }); console.log(data) });
    }

    onChange(newValue) {
        console.log(newValue);
    }

    /* https://github.com/securingsincity/react-ace */

    render() {

        const aceStyle = {
            border: '1px solid #ccc',
            width: '85%',
            boxShadow: '0px 5px 7px 0px #ccc'
        }

        const problemName = <h2>Problem 1 : Calculating Prime Numbers</h2>;

        const problemDescription = <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Totam incidunt tempora quo, assumenda vitae nobis! Voluptates dolore placeat architecto enim quis ad, corporis assumenda amet laudantium praesentium. Expedita in optio reiciendis eum repellendus adipisci labore ipsam quos quia porro eaque, aspernatur incidunt temporibus dicta iure qui nemo distinctio nobis, velit, repudiandae amet! Neque nobis accusantium voluptatibus distinctio, incidunt, est fugiat voluptate fugit quae atque perferendis vitae magnam optio repellat dicta? Doloremque, eligendi tempore. Tenetur iste pariatur dolores quibusdam enim. Impedit, voluptates. Ad voluptatum suscipit laboriosam ut, quo dignissimos sunt amet totam nam! Necessitatibus modi, sequi dignissimos nostrum dolor porro inventore harum, illum a natus veniam quae beatae ut! Nostrum consequatur ducimus vitae, quibusdam est minus provident asperiores deserunt eligendi? Veniam unde modi eligendi commodi sed? Modi quo doloribus saepe ea quaerat vitae distinctio unde veritatis libero eaque cupiditate corrupti tempore amet quis atque reprehenderit, et voluptate inventore reiciendis, sint, dolores quas? Quod harum doloribus esse, nam obcaecati vero voluptas suscipit repellendus culpa, libero molestiae dolorem facere expedita, quibusdam excepturi deleniti dolore modi ipsa magni? Enim consequatur aliquam explicabo aut rem veritatis provident, quod aperiam unde repellendus velit odio tempora, eum fuga doloribus sapiente? Cupiditate quo itaque, sapiente atque magnam earum?</p>

        return (
            <div className="container">
                <div className="row">
                    <h2 className="page-title"> {this.state.problem.name}</h2>
                    <hr />
                    <div className="col-sm-10 problem-description-container ">
                        <p>{this.state.problem.description}</p>
                        <p>Lorem ipsum, dolor sit amet consectetur adipisicing elit. Doloremque voluptate officia eum natus facilis ratione pariatur et iusto amet numquam reiciendis non nesciunt quaerat ipsam eveniet neque voluptatem corporis, possimus mollitia. Ratione animi mollitia asperiores dolore perferendis. Sapiente aliquid eaque nobis officiis sit aspernatur earum cupiditate atque. Hic nemo, inventore, dolorem quisquam architecto eius in nulla quia quo magni itaque accusantium. Iste excepturi maiores veritatis omnis itaque saepe hic est reprehenderit? Explicabo aliquid temporibus, atque dolore numquam excepturi sit iusto, debitis deleniti ex impedit quas voluptas quia! Molestias totam, minus labore quae aliquid explicabo accusamus nostrum magnam architecto laborum provident impedit temporibus error, nihil voluptate magni, quisquam et praesentium deserunt unde aperiam? Voluptatum, quasi quidem dignissimos alias atque fuga omnis voluptate veritatis recusandae nostrum excepturi odio optio ducimus dicta quos aliquid tempora cupiditate autem fugiat, ab aspernatur enim? Delectus illo reiciendis nemo magni, ab maiores qui sed! Nihil, quisquam explicabo! Itaque sapiente odit quae. Eum voluptates et error amet aliquid quibusdam veniam reprehenderit doloribus. Vitae earum cumque animi excepturi eveniet reiciendis rerum commodi nisi quos, repellat ipsam non veniam eos ea repellendus. Voluptate amet harum mollitia et magni quo officiis veniam, rerum odit quis magnam ducimus laborum quae, quod molestiae sequi consectetur. Labore dolore aliquam earum quidem consequatur dignissimos voluptate temporibus. Reiciendis esse fugit, debitis iusto consequuntur at nisi ducimus repellat hic cum aspernatur ab obcaecati exercitationem inventore, expedita accusamus pariatur quos beatae est in. Ipsam similique ab doloremque! Non dolore reiciendis, aut ducimus, esse inventore sunt odit dicta sed beatae eaque id? Quod at, culpa eligendi fugit, ex mollitia reprehenderit deserunt minima unde, consequuntur commodi adipisci sapiente doloribus odio enim iusto? Autem officia tempora sint ut magnam inventore cumque recusandae sapiente rem molestias sit obcaecati natus itaque, aliquid perferendis earum quaerat ducimus, modi sed facere assumenda ex. Reprehenderit, dolore!</p>
                    </div>
                    <div className="col-sm-2 problem-info-container ">
                        <p>USER</p>
                        <p>DIFFICULTY</p>
                    </div>
                </div>

                <div className="row">
                    <div className="col-sm-12">
                        <div className="ace-editor">
                            <div className="ace-editor-navbar">
                                <p>Language</p>
                                <p>Theme</p>
                            </div>
                            <AceEditor
                                style={aceStyle}
                                mode="javascript"
                                theme="tomorrow"
                                name=""
                                onLoad={this.onLoad}
                                onChange={this.onChange}
                                fontSize={14}
                                showPrintMargin={true}
                                showGutter={true}
                                highlightActiveLine={true}
                                value={`function onLoad(editor) {
  console.log("i've loaded");
  console.log()
}`}
                                setOptions={{
                                    enableBasicAutocompletion: true,
                                    enableLiveAutocompletion: true,
                                    enableSnippets: false,
                                    showLineNumbers: true,
                                    tabSize: 2,
                                }} />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Problem;

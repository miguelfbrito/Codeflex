import React from 'react';

class CompilerError extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {

        const error = this.props.errorMessage.split('\n').map((item, key) => {
            return <span key={key}>{item}<br /></span>
        });

        return (
            <div>
                <p>Compiler Error</p>
                <p>{error}</p>
            </div>
        );
    }
}

export default CompilerError;
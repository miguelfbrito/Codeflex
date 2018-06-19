import React from 'react';
import Modal from 'react-modal';

import './Popup.css';

const customStyles = {
    content: {
        top: '50%',
        left: '50%',
        right: 'auto',
        bottom: 'auto',
        marginRight: '-50%',
        transform: 'translate(-50%, -50%)',
        backgroundColor: 'white',
        boxShadow: '0 0 5px #aaa'
    }
};

// Make sure to bind modal to your appElement (http://reactcommunity.org/react-modal/accessibility/)
Modal.setAppElement('#root')

class Popup extends React.Component {

    constructor() {
        super();

        this.state = {
            modalIsOpen: false
        };

        this.openModal = this.openModal.bind(this);
        this.afterOpenModal = this.afterOpenModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
    }

    openModal() {
        this.setState({ modalIsOpen: true });
     //   setTimeout(() => (this.setState({ modalIsOpen: false })), this.props.timeoutClose);
    }

    afterOpenModal() {
    }

    closeModal() {
        this.setState({ modalIsOpen: false });
        this.props.onModalClose();
    }
    render() {

        const children = this.props.children;

        return (
            <div>
                {/*<button onClick={this.openModal}>Open Modal</button>*/}
                <Modal
                    isOpen={this.state.modalIsOpen}
                    onAfterOpen={this.afterOpenModal}
                    onRequestClose={this.closeModal}
                    style={customStyles}
                    contentLabel="Example Modal"
                >
                    {children}
                </Modal>
            </div>
        );
    }
}

Popup.defaultProps = {
    title: "Placeholder Title",
    message: "Placeholder Message",
    timeoutClose: 999999
}


export default Popup;
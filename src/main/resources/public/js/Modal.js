window.Modal = React.createClass({
    getDefaultProps: function () {
        return {
            body: "Bodal Body",
            onClear: function () {

            },
            okClick: function () {
            }
        };
    },
    render: function () {

        return (
            <div>

                <div className="modal fade" id="myModal" tabIndex="-1" role="dialog" aria-labelledby="myModalLabel">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                                <h4 className="modal-title" id="myModalLabel">Modal title</h4>
                            </div>
                            <div className="modal-body">


                                {this.props.body}


                            </div>

                            <div className="modal-footer">
                                <span type="button" className="btn btn-danger" data-dismiss="modal">Cancel</span>
                                <span type="button" className="btn btn-info"
                                      onClick={this.props.onClear}>Clear</span>
                                <span type="button" className="btn btn-primary" data-dismiss="modal"
                                      onClick={this.props.okClick}>Ok</span>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        );
    }
});
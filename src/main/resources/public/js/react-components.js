window.AjaxSelect = React.createClass({
    getInitialState: function () {
        var _self = this;
        $("body").on("value-selected", function (e, data) {
            _self.setState({value: data});
        });

        return {};
    },
    handleClick: function () {
        $('.modal').modal('show');
    },
    render: function () {
        return (
            <input id="ajax-select" className="form-control ajax-select" onClick={this.handleClick}
                   value={this.state.value}></input>
        );
    }
});

window.AjaxSelectBox = React.createClass({
    getDefaultProps: function () {
        return {
            defaultSize: 100,
            navLength: 5
        };
    },
    getInitialState: function () {
        return {data: [], pagination: {page: 1, size: 20, total: 0}};
    },

    componentDidMount: function () {
        this.requestData();
    },

    onComplete: function (e, data) {
        $('.modal').modal('hide');
        $("body").trigger("value-selected", data);
    },

    handleClick: function (e) {
        this.onComplete(e, $(e.target).attr("data-key"));
    },

    handleSubmit: function (e) {
        e.preventDefault();
        this.onComplete(e, $("#ajax-145415").val());
    },

    handleKeyEvent: function (e) {
        console.log("aaaa : " + e.target);

        setTimeout(function () {

            this.requestData();

        }.bind(this), 100);

    },

    clear: function () {
        console.log("clearing....");
        this.setState({value: ""}, function () {
            this.requestData(1);
        }.bind(this));
    },

    handleValueChange: function (e) {
        this.setState({value: e.target.value});
    },

    handleOkClick: function (e) {
        this.onComplete(e, $("#ajax-145415").val());
    },

    onPageRequest: function (page, size) {
        this.requestData(page, size);
    },

    render: function () {
        var _self = this;

        var rows = this.state.data.map(function (d) {
            d = {key: d.CLUSTER_NAME, value: d.CLUSTER_NAME}
            return (
                <tr key={'row-' + d.key} onClick={_self.handleClick}>
                    <td data-key={d.key}>{d.value}</td>
                </tr>
            );
        });

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


                                <div id="ajax-select-box" className="ajax-select-box">


                                    <form onSubmit={this.handleSubmit}>
                                        <div id="search-input" className="form-group">
                                            <input id="ajax-145415" type="text" className="form-control"
                                                   placeholder="Search" onKeyUp={this.handleKeyEvent}
                                                   onChange={this.handleValueChange}
                                                   value={this.state.value}/>
                                        </div>
                                    </form>

                                    <div className="table-responsive" style={{maxHeight: '350px'}}>
                                        <table className="table table-bordered table-striped table-hover">

                                            <colgroup>
                                                <col className="col-xs-8"/>
                                            </colgroup>

                                            <tbody>

                                            {rows}

                                            </tbody>
                                        </table>
                                    </div>


                                    <Pagination page={this.state.pagination.page}
                                                size={this.state.pagination.size}
                                                total={this.state.pagination.total}
                                                navLength={this.props.navLength}
                                                defaultSize={this.props.defaultSize}
                                                onPageRequest={this.onPageRequest}/>

                                </div>


                            </div>

                            <div className="modal-footer">
                                <span type="button" className="btn btn-danger" data-dismiss="modal">Cancel</span>
                                <span type="button" className="btn btn-info"
                                      onClick={this.clear}>Clear</span>
                                <span type="button" className="btn btn-primary" data-dismiss="modal"
                                      onClick={this.handleOkClick}>Ok</span>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        );
    },

    requestData: function (page, size) {
        ajax({
            url: "/search-cluster",
            data: {name: $("#ajax-145415").val(), page: page, size: size},
            dataType: 'json',
            cache: false,
            success: function (json) {

                this.setState({data: json.data, pagination: json.pagination});

            }.bind(this),
            error: function (xhr, status, err) {
                alert(err.toString());
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    }
});
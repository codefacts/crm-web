window.AjaxSelect = React.createClass({
    getDefaultProps: function () {
        return {
            defaultValue: ""
        };
    },
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
                   defaultValue={this.props.defaultValue}
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
        return {value: "", data: [], pagination: {page: 1, size: 20, total: 0}};
    },

    componentDidMount: function () {
        this.requestData(this.state.value);
    },

    onComplete: function (data) {
        $('.modal').modal('hide');
        $("body").trigger("value-selected", data);
    },

    handleClick: function (e) {
        this.setState({value: $(e.target).attr("data-key")});
        this.onComplete($(e.target).attr("data-key"));
    },

    handleSubmit: function (e) {
        e.preventDefault();
        this.onComplete($("#ajax-145415").val());
    },

    handleKeyEvent: function (e) {
        console.log("aaaa : " + e.target);

        setTimeout(function () {

            this.requestData(e.target.value);

        }.bind(this), 100);

    },

    clear: function () {
        console.log("clearing....");
        this.setState({value: ""}, function () {
            this.requestData("", 1);
        }.bind(this));
    },

    handleValueChange: function (e) {
        this.setState({value: e.target.value});
    },

    handleOkClick: function (e) {
        this.onComplete($("#ajax-145415").val());
    },

    onPageRequest: function (page, size) {
        this.requestData(this.state.value, page, size);
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

        var modalBody = (
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
        );

        return (
            <Modal body={modalBody} onClear={this.clear} okClick={this.handleOkClick}/>
        );
    },

    requestData: function (name, page, size) {
        ajax({
            url: "/search-cluster",
            data: {name: name, page: page || 1, size: size || this.props.defaultSize},
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
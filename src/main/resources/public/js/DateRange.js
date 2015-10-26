window.DateRange = React.createClass({

    getDefaultProps: function () {
        return {
            id: "",
            placeholder: "",
            title: "",
            from: "",
            to: "",
            modalId: "",
            modalTitle: "",
            name: ""
        };
    }
    ,

    getInitialState: function () {
        if (!!this.props.value) {
            var splits = this.props.value.split(":", 2)
            splits = splits.length < 2 ? ["", ""] : splits;
            return {
                from: splits[0].trim(),
                to: splits[1].trim(),
                value: (((this.props.from != "") || (this.props.to != "")) ? (this.props.from + " : " + this.props.to) : "")
            };
        }
        return {
            from: this.props.from,
            to: this.props.to,
            value: (((this.props.from != "") || (this.props.to != "")) ? (this.props.from + " : " + this.props.to) : "")
        };
    }
    ,

    componentDidMount: function () {
        console.log("Component");
        $('.date-range-from').datepicker(global_date_picker_config)
            .on("changeDate", function (e) {
                console.log(e);
                this.setState({from: formatDate(e.date)});
            }.bind(this));

        $('.date-range-to').datepicker(global_date_picker_config)
            .on("changeDate", function (e) {
                console.log(e);
                this.setState({to: formatDate(e.date)});
            }.bind(this));
    },

    onFromChange: function (e) {
        this.setState({from: e.target.value});
    }
    ,

    onToChange: function (e) {
        this.setState({to: e.target.value});
    }
    ,

    onClear: function (e) {
        this.setState({from: "", to: ""});
    }
    ,

    okClick: function (e) {
        this.setState({
            value: (this.state.from + " : " + this.state.to)
        });
    }
    ,

    onClick: function (e) {
        $('#' + this.props.modalId).modal('show');
    },

    render: function () {

        var modalBody = (

            <div className="row">

                <div className="col-md-6">
                    <div className="form-group">
                        <label htmlFor="exampleInputEmail1">From</label>
                        <input type="text" className="form-control date-range-from" id="exampleInputEmail1"
                               placeholder="From"
                               value={this.state.from}
                               onChange={this.onFromChange}/>
                    </div>
                </div>

                <div className="col-md-6">
                    <div className="form-group">
                        <label htmlFor="exampleInputEmail1">To</label>
                        <input type="text" className="form-control date-range-to" id="exampleInputEmail1"
                               placeholder="To"
                               value={this.state.to}
                               onChange={this.onToChange}/>
                    </div>
                </div>

            </div>

        );

        var value = (((this.state.from != "") || (this.state.to != "")) ? (this.state.from + " : " + this.state.to) : "");

        return (
            <div className="form-group">
                <input id={this.props.id} type="text" className="form-control range" placeholder={this.props.placeholder}
                       title={this.props.title}
                       name={this.props.name}
                       value={value}
                       onClick={this.onClick}/>
                <Modal body={modalBody} onClear={this.onClear} okClick={this.okClick} id={this.props.modalId}
                       title={this.props.modalTitle}/>
            </div>
        );
    }
})
;
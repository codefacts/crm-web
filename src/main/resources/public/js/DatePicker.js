window.DatePicker = React.createClass({

    getDefaultProps: function () {
        return {
            value: ""
        };
    }
    ,

    getInitialState: function () {
        return {
            value: this.props.value
        };
    }
    ,

    componentDidMount: function () {
        console.log("Component");

        $('.datepicker').datepicker(global_date_picker_config)
            .on("changeDate", function (e) {
                console.log(formatDate(e.date));
                this.setState({value: formatDate(e.date)});
            }.bind(this));
    },

    onChange: function (e) {
        console.log(e.target.value);
        this.setState({
            value: e.target.value
        });
    },

    render: function () {
        console.log("value: " + this.state.value)
        return (
            <div className="form-group">
                <input id="call_range" type="text" className="form-control datepicker" placeholder="Call No. Between"
                       name="call_range"
                       value={this.state.value}
                       onChange={this.onChange}/>
            </div>
        );
    }
})
;
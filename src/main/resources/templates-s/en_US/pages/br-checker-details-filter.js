(function () {
    var Form = React.createClass({
        render: function () {
            var modalCounter = 1;
            return (
                <form>

                    <div className="row">
                        <div className="col-md-3">
                            <AjaxSelectBox modalId={"filter-modal-" + modalCounter++}
                                           modalTitle="Please Select Cluster Name"
                                           placeholder="Cluster Name"
                                           url="~searchClusterUrl~"
                                           name="CLUSTER_NAME"
                                           value="~CLUSTER_NAME~"
                                           keyValueParser={function (obj) {return {key: obj.CLUSTER_NAME, value: obj.CLUSTER_NAME}}}/>
                        </div>

                        <div className="col-md-2">
                            <AjaxSelectBox modalId={"filter-modal-" + modalCounter++}
                                           modalTitle="Please Select TSR Code"
                                           placeholder="TSR Code"
                                           url="~searchTsrCodeUrl~"
                                           name="TSR_CODE"
                                           value="~TSR_CODE~"
                                           keyValueParser={function (obj) {return {key: obj.TSR_CODE, value: (obj.TSR_CODE + ":" + obj.TSR_NAME)}}}/>
                        </div>

                        <div className="col-md-2">
                            <AjaxSelectBox modalId={"filter-modal-" + modalCounter++}
                                           modalTitle="Please Select Auditor Code"
                                           placeholder="Auditor Code"
                                           url="~searchAuditorCodeUrl~"
                                           name="AUDITOR_CODE"
                                           value="~AUDITOR_CODE~"
                                           keyValueParser={function (obj) {return {key: obj.AUDITOR_CODE, value: (obj.AUDITOR_CODE + ":" + obj.AUDITOR_NAME)}}}/>
                        </div>

                        <div className="col-md-3">
                            <AjaxSelectBox modalId={"filter-modal-" + modalCounter++}
                                           modalTitle="Please Select Consumer Name"
                                           placeholder="Consumer Name"
                                           url="~searchConsumerNameUrl~"
                                           name="CONSUMER_NAME"
                                           value="~CONSUMER_NAME~"
                                           keyValueParser={function (obj) {return {key: obj.CONSUMER_NAME, value: obj.CONSUMER_NAME}}}/>
                        </div>

                        <div className="col-md-2">
                            <AjaxSelectBox modalId={"filter-modal-" + modalCounter++}
                                           modalTitle="Please Select Consumer Mobile"
                                           placeholder="Consumer Mobile"
                                           url="~searchConsumerMobileUrl~"
                                           name="CONSUMER_MOBILE_NUMBER"
                                           value="~CONSUMER_MOBILE_NUMBER~"
                                           keyValueParser={function (obj) {return {key: obj.CONSUMER_MOBILE_NUMBER, value: obj.CONSUMER_MOBILE_NUMBER}}}/>
                        </div>

                    </div>

                    <div className="row">

                        <div className="col-md-2">
                            <Range modalId={"filter-modal-" + modalCounter++}
                                   modalTitle="Please Select Call Count Between"
                                   name="CALL_NO_RANGE"
                                   value="~CALL_NO_RANGE~"
                                   placeholder="Call Count Range" from="" to=""/>
                        </div>

                        <div className="col-md-2">
                            <Range modalId={"filter-modal-" + modalCounter++}
                                   modalTitle="Please Select Visit Count Between"
                                   name="TOTAL_VISITED_RANGE"
                                   value="~TOTAL_VISITED_RANGE~"
                                   placeholder="Visit Count Range" from="" to=""/>
                        </div>

                        <div className="col-md-2">

                            <div className="form-group">

                                <select className="form-control" defaultValue="~BAND~" name="BAND">
                                    <option value="">Brand Match</option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>

                            </div>

                        </div>

                        <div className="col-md-2">

                            <div className="form-group">

                                <select className="form-control" defaultValue="~CONTACTED~" name="CONTACTED">
                                    <option value="">Contacted</option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>

                            </div>

                        </div>

                        <div className="col-md-2">

                            <div className="form-group">

                                <select className="form-control" defaultValue="~NAME_MATCH~" name="NAME_MATCH">
                                    <option value="">Name Match</option>
                                    <option value="1">Yes</option>
                                    <option value="0">No</option>
                                </select>

                            </div>

                        </div>

                        <div className="col-md-2">

                            <div className="form-group">

                                <select className="form-control" defaultValue="~CALL_STATUS~" name="CALL_STATUS">
                                    <option value="">Call Status</option>
                                    ~for cs in callStatuses:
                                    <option value="~cs~">~cs~</option>
                                    :~
                                </select>

                            </div>

                        </div>

                    </div>

                    <div className="row">

                        <div className="col-md-4">

                            <DateRange modalId={"filter-modal-" + modalCounter++}
                                       name="DATE_AND_TIME_RANGE"
                                       value="~DATE_AND_TIME_RANGE~"
                                       modalTitle="Please Select Date Range"/>

                        </div>

                        <div id="" className="col-md-8">

                            <button id="" type="submit" className="btn btn-primary pull-right btn-form-footer"
                                    name="__action__" value="search">Search
                            </button>

                            <button id="" type="submit" className="btn btn-danger pull-right btn-form-footer"
                                    name="__action__" value="clear">Clear
                            </button>

                        </div>

                    </div>

                </form>
            );
        }
    });

    ReactDOM.render(<Form/>, document.getElementById("filters-div"));
})();
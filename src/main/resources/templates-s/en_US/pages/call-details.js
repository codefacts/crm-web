(function () {

    ~callDetailsSummaryView~

    var CallDetails = React.createClass({
        render: function () {
            return (
                    <div>

                        <div className="panel panel-default">
                          <div className="panel-body">

                            <form id="filters">

                                <div className="row">

                                    <div className="col-md-3">
                                          <div className="form-group">
                                            <select className="form-control" defaultValue="" title="Select Area">
                                                <option>Select Area</option>
                                            </select>
                                          </div>
                                    </div>

                                    <div className="col-md-3">
                                          <div className="form-group">
                                            <select className="form-control" defaultValue="" title="Select House">
                                                <option>Select House</option>
                                            </select>
                                          </div>
                                    </div>

                                    <div className="col-md-3">
                                          <div className="form-group">
                                            <select className="form-control" defaultValue="" title="Select BR">
                                                <option>Select BR</option>
                                            </select>
                                          </div>
                                    </div>

                                    <div className="col-md-3">
                                        <DateRange modalId="md-1" modalTitle="Select date range" placeholder="Date Range" title="Date Range"/>
                                    </div>

                                    <div className="col-md-2">
                                          <div className="form-group">
                                                <Range modalId="md-2" placeholder="Contact Range" title="Contact Range"/>
                                          </div>
                                    </div>

                                    <div className="col-md-2">
                                          <div className="form-group">
                                                <Range modalId="md-6" placeholder="Success Range" title="Success Range"/>
                                          </div>
                                    </div>

                                    <div className="col-md-2">
                                          <div className="form-group">
                                            <select className="form-control" defaultValue="" title="Call Status">
                                                <option>Call Status</option>
                                                <option value="">Called</option>
                                                <option>Not Called</option>
                                            </select>
                                          </div>
                                    </div>

                                    <div className="col-md-6">

                                        <button id="" type="submit" className="btn btn-primary pull-right btn-form-footer"
                                                name="__action__" value="search">Search
                                        </button>

                                        <button id="" type="submit" className="btn btn-danger pull-right btn-form-footer"
                                                name="__action__" value="clear">Clear
                                        </button>

                                    </div>

                                </div>

                            </form>

                          </div>
                        </div>


                        <div className="row">

                            <div className="col-md-3">

                                <CallDetailsSummaryView />

                            </div>
                            <div className="col-md-9">

                                <div className="panel panel-default">
                                  <div className="panel-body">

                                    <div id="container" className="row">

                                        <div className="col-md-12">

                                            <div className="table-responsive">
                                              <table className="table table-stripped table-bordered table-hover">

                                                <thead>
                                                <tr>
                                                    <th>BR ID</th>
                                                    <th>BR Name</th>
                                                    <th>Active</th>
                                                    <th>Work Date</th>
                                                    <th>Contact</th>
                                                    <th>PTR</th>
                                                    <th>REF.</th>
                                                    <th>G.A.</th>
                                                    <th>CALL</th>
                                                    <th>SUCCESS</th>
                                                </tr>
                                                </thead>

                                                <tbody>

                                                <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                    <tr>
                                                    <td>BR ID</td>
                                                    <td>BR Name</td>
                                                    <td>Active</td>
                                                    <td>Work Date</td>
                                                    <td>Contact</td>
                                                    <td>PTR</td>
                                                    <td>REF.</td>
                                                    <td>G.A.</td>
                                                    <td>CALL</td>
                                                    <td>SUCCESS</td>
                                                    </tr>

                                                </tbody>

                                              </table>
                                            </div>

                                        </div>

                                    </div>

                                  </div>
                                </div>

                            </div>

                        </div>

                    </div>
            );
        }
    });

    ReactDOM.render(<CallDetails />, document.getElementById('container'));
})();
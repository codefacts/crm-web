window.Pagination = React.createClass({
    getDefaultProps: function () {
        return {
            page: 1,
            size: 100,
            total: 0,
            navLength: 5,
            onPageRequest: function (e) {
                console.log(e)
            },
            style: {
                marginBottom: '10px',
                marginTop: '10px'
            }
        };
    },

    getInitialState: function () {
        return {};
    },

    onPageClick: function (e) {
        this.handleRequest(e);
    },

    onPrevClick: function (e) {
        this.handleRequest(e);
    },

    onNextClick: function (e) {
        this.handleRequest(e);
    },

    handleRequest: function (e) {
        this.props.onPageRequest($(e.target).attr("data-page"), this.props.size);
    },

    render: function () {
        var _self = this;

        var xxx = {
            page: parseInt(this.props.page),
            size: parseInt(this.props.size),
            total: parseInt(this.props.total),
            navLength: parseInt(this.props.navLength)
        };

        var pu = PaginationUtil(xxx);
        console.log(pu);

        var list = pu.nav().map(function (p) {
            return (
                <li key={p} className={pu.isCurrentPage(p) ? "active" : ""}>
                    <span data-page={p} onClick={_self.onPageClick}>{p}</span>
                </li>
            );
        });

        return (

            <nav>
                <ul className="pagination pagination-sm" style={this.props.style}>
                    <li className={!pu.hasPrev() ? "disabled" : ""}>
                        <a href="#" aria-label="Prev" data-page={pu.prev()} onClick={this.onPrevClick}>
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    {list}
                    <li className={!pu.hasNext() ? "disabled" : ""}>
                        <a href="#" aria-label="Next" data-page={pu.next()} onClick={this.onNextClick}>
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        );
    }
})
;
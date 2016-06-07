(function() {
    'use strict';

    angular
        .module('tlcrmspaApp')
        .factory('SupplierSearch', SupplierSearch);

    SupplierSearch.$inject = ['$resource'];

    function SupplierSearch($resource) {
        var resourceUrl =  'api/_search/suppliers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

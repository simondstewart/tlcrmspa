(function() {
    'use strict';
    angular
        .module('tlcrmspaApp')
        .factory('Supplier', Supplier);

    Supplier.$inject = ['$resource', 'DateUtils'];

    function Supplier ($resource, DateUtils) {
        var resourceUrl =  'api/suppliers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateCreated = DateUtils.convertLocalDateFromServer(data.dateCreated);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateCreated = DateUtils.convertLocalDateToServer(data.dateCreated);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateCreated = DateUtils.convertLocalDateToServer(data.dateCreated);
                    return angular.toJson(data);
                }
            }
        });
    }
})();

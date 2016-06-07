(function() {
    'use strict';
    angular
        .module('tlcrmspaApp')
        .factory('Client', Client);

    Client.$inject = ['$resource'];

    function Client ($resource) {
        var resourceUrl =  'api/clients/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

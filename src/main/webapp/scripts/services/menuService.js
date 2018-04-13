/**
 * Created by zhangyongliang on 6/8/15.
 */
/**
 * Created by zhangyongliang on 6/8/15.
 */

define([
    './module'
], function (services) {
    'use strict';
    services.factory('menu', [
        '$location',
        '$rootScope',
        'baseService',
        function ($location, $rootScope,baseService) {

            var version = {};

            var sections = [];

            baseService.post("/index/getUser", {}).then(
                function (data) {
                    if(!angular.isUndefined(data.result)){
                        baseService.post("/sysmenu/getUserMenu", {
                            user_name : data.result.user_name
                        }).then(
                            function (data) {
                                for(var i=0;i<data.result.length;i++)
                                    sections.push(data.result[i]);
                            }
                        )
                    }
                }
            )

            var self;

            $rootScope.$on('$locationChangeSuccess', onLocationChange);

            return self = {
                version: version,
                sections: sections,

                selectSection: function (section) {
                    self.openedSection = section;
                },
                toggleSelectSection: function (section) {
                    self.openedSection = (self.openedSection === section ? null : section);
                },
                isSectionSelected: function (section) {
                    return self.openedSection === section;
                },

                selectPage: function (section, page) {
                    self.currentSection = section;
                    self.currentPage = page;
                },
                isPageSelected: function (page) {
                    return self.currentPage === page;
                }
            };

            function sortByHumanName(a, b) {
                return (a.humanName < b.humanName) ? -1 :
                    (a.humanName > b.humanName) ? 1 : 0;
            }

            function onLocationChange() {
                var path = $location.path();

                if (path == '/') {
                    self.selectSection(null);
                    self.selectPage(null, null);
                    return;
                }

                var matchPage = function (section, page) {
                    if (path === page.menu_url) {
                        self.selectSection(section);
                        self.selectPage(section, page);
                    }
                };

                sections.forEach(function (section) {
                    if (section.children) {
                        // matches nested section toggles, such as API or Customization
                        section.children.forEach(function (childSection) {
                            if (childSection.pages) {
                                childSection.pages.forEach(function (page) {
                                    matchPage(childSection, page);
                                });
                            }
                        });
                    }
                    else if (section.pages) {
                        // matches top-level section toggles, such as Demos
                        section.pages.forEach(function (page) {
                            matchPage(section, page);
                        });
                    }
                    else if (section.menu_type === 'link') {
                        // matches top-level links, such as "Getting Started"
                        matchPage(section, section);
                    }
                });
            }
        }])

});

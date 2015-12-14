App.controller('QueryController',[ '$scope', '$http','$timeout','$location','QueryService', function($scope, $http,$timeout,$location,QueryService) {
    $scope.availableSearchParams = [
          { key: "school", name: "School", placeholder: "School..." },
          { key: "job", name: "Job", placeholder: "Job..." },
          { key: "skill", name: "Skill", placeholder: "Skill..." },
          { key: "number", name: "resultNumber", placeholder: "resultNumber..." },
        ];
        
    $scope.$on('$viewContentLoaded', function(event) {
      $scope.formData = {value: 'null'};
      $timeout(function() {
            $scope.formData.value = document.getElementById("morphsearch").querySelector('input.morphsearch-input').toString();
            var morphSearch = document.getElementById( 'morphsearch' ),
//            input = morphSearch.querySelector('input.search-parameter-input'),
            input = morphSearch.querySelector( 'input.morphsearch-input' ),
            ctrlClose = morphSearch.querySelector( 'span.morphsearch-close' ),
            isOpen = isAnimating = false,
            // show/hide search area
            toggleSearch = function(evt) {
                    // return if open and the input gets focused
                    if( evt.type.toLowerCase() === 'focus' && isOpen ) return false;

                    var offsets = morphsearch.getBoundingClientRect();
                    if( isOpen ) {
                            classie.remove( morphSearch, 'open' );

                            // trick to hide input text once the search overlay closes 
                            // todo: hardcoded times, should be done after transition ends
                            if( input.value !== '' ) {
                                    setTimeout(function() {
                                            classie.add( morphSearch, 'hideInput' );
                                            setTimeout(function() {
                                                    classie.remove( morphSearch, 'hideInput' );
                                                    input.value = '';
                                            }, 300 );
                                    }, 500);
                            }

                            input.blur();
                    }
                    else {
                            classie.add( morphSearch, 'open' );
                    }
                    isOpen = !isOpen;
            };

            // events
            input.addEventListener( 'focus', toggleSearch );
            ctrlClose.addEventListener( 'click', toggleSearch );
            // esc key closes search overlay
            // keyboard navigation events
            document.addEventListener( 'keydown', function( ev ) {
                    var keyCode = ev.keyCode || ev.which;
                    if( keyCode === 27 && isOpen ) {
                            toggleSearch(ev);
                    }
            } );

            /***** for demo purposes only: don't allow to submit the form *****/
        morphSearch.querySelector( 'button[type="submit"]' ).addEventListener( 'click', function(ev) { ev.preventDefault(); } );
      },100);
      
      
    });
    
    
    $scope.addPredefinedNameSearchParam = function(){
        $scope.list = [];
        var formData = {
                        "university" : $scope.searchParams.school,
                        "job" : $scope.searchParams.job,
                        "skill" : $scope.searchParams.skill,
                        'query': $scope.searchParams.query,
                        'number': $scope.searchParams.number
        };
//        $state.params = formData;
//        $scope.state = $state;
        QueryService.set(formData);
        $location.path("search");
        };
//    $scope.go = function(path) {
//    $location.path("/search");
//  };
        $scope.loadPredefinedSearchParamSet = function(){
            var response = $http.get('http://localhost:8080/IRProject/query/');
//          $scope.searchParams = {
//            name: "Max M.",
//            job: "Boss"
//          };
        };
      }])
  
  .directive('openCloseController', ['$document', function($document) {
  return {
    link: function(scope, element, attr) {
      var startX = 0, startY = 0, x = 0, y = 0;

      element.css({
       position: 'relative',
       border: '1px solid red',
       backgroundColor: 'lightgrey',
       cursor: 'pointer'
      });

      element.on('mousedown', function(event) {
        // Prevent default dragging of selected content
        event.preventDefault();
        startX = event.pageX - x;
        startY = event.pageY - y;
        $document.on('mousemove', mousemove);
        $document.on('mouseup', mouseup);
      });

      function mousemove(event) {
        y = event.pageY - startY;
        x = event.pageX - startX;
        element.css({
          top: y + 'px',
          left:  x + 'px'
        });
      }

      function mouseup() {
        $document.off('mousemove', mousemove);
        $document.off('mouseup', mouseup);
      }
    }
  };
}]);
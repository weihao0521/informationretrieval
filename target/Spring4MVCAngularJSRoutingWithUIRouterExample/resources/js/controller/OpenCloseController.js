//App.directive('openCloseController', ['', function(){
//    return{
//         link: function(scope, element, attr) {
//             var	aux		= {
//			// navigates left / right
//			navigate	: function( dir, $el, $wrapper, opts, cache ) {
//				
//				var scroll		= opts.scroll,
//					factor		= 1,
//					idxClicked	= 0;
//					
//				if( cache.expanded ) {
//					scroll		= 1; // scroll is always 1 in full mode
//					factor		= 3; // the width of the expanded item will be 3 times bigger than 1 collapsed item	
//					idxClicked	= cache.idxClicked; // the index of the clicked item
//				}
//				
//				// clone the elements on the right / left and append / prepend them according to dir and scroll
//				if( dir === 1 ) {
//					$wrapper.find('div.ca-item:lt(' + scroll + ')').each(function(i) {
//						$(this).clone(true).css( 'left', ( cache.totalItems - idxClicked + i ) * cache.itemW * factor + 'px' ).appendTo( $wrapper );
//					});
//				}
//				else {
//					var $first	= $wrapper.children().eq(0);
//					
//					$wrapper.find('div.ca-item:gt(' + ( cache.totalItems  - 1 - scroll ) + ')').each(function(i) {
//						// insert before $first so they stay in the right order
//						$(this).clone(true).css( 'left', - ( scroll - i + idxClicked ) * cache.itemW * factor + 'px' ).insertBefore( $first );
//					});
//				}
//				
//				// animate the left of each item
//				// the calculations are dependent on dir and on the cache.expanded value
//				$wrapper.find('div.ca-item').each(function(i) {
//					var $item	= $(this);
//					$item.stop().animate({
//						left	:  ( dir === 1 ) ? '-=' + ( cache.itemW * factor * scroll ) + 'px' : '+=' + ( cache.itemW * factor * scroll ) + 'px'
//					}, opts.sliderSpeed, opts.sliderEasing, function() {
//						if( ( dir === 1 && $item.position().left < - idxClicked * cache.itemW * factor ) || ( dir === -1 && $item.position().left > ( ( cache.totalItems - 1 - idxClicked ) * cache.itemW * factor ) ) ) {
//							// remove the item that was cloned
//							$item.remove();
//						}						
//						cache.isAnimating	= false;
//					});
//				});
//				
//			},
//			// opens an item (animation) -> opens all the others
//			openItem	: function( $wrapper, $item, opts, cache ) {
//				cache.idxClicked	= $item.index();
//				// the item's position (1, 2, or 3) on the viewport (the visible items) 
//				cache.winpos		= aux.getWinPos( $item.position().left, cache );
//				$wrapper.find('div.ca-item').not( $item ).hide();
//				$item.find('div.ca-content-wrapper').css( 'left', cache.itemW + 'px' ).stop().animate({
//					width	: cache.itemW * 2 + 'px',
//					left	: cache.itemW + 'px'
//				}, opts.itemSpeed, opts.itemEasing)
//				.end()
//				.stop()
//				.animate({
//					left	: '0px'
//				}, opts.itemSpeed, opts.itemEasing, function() {
//					cache.isAnimating	= false;
//					cache.expanded		= true;
//					
//					aux.openItems( $wrapper, $item, opts, cache );
//				});
//						
//			},
//			// opens all the items
//			openItems	: function( $wrapper, $openedItem, opts, cache ) {
//				var openedIdx	= $openedItem.index();
//				
//				$wrapper.find('div.ca-item').each(function(i) {
//					var $item	= $(this),
//						idx		= $item.index();
//					
//					if( idx !== openedIdx ) {
//						$item.css( 'left', - ( openedIdx - idx ) * ( cache.itemW * 3 ) + 'px' ).show().find('div.ca-content-wrapper').css({
//							left	: cache.itemW + 'px',
//							width	: cache.itemW * 2 + 'px'
//						});
//						
//						// hide more link
//						aux.toggleMore( $item, false );
//					}
//				});
//			},
//			// show / hide the item's more button
//			toggleMore	: function( $item, show ) {
//				( show ) ? $item.find('a.ca-more').show() : $item.find('a.ca-more').hide();	
//			},
//			// close all the items
//			// the current one is animated
//			closeItems	: function( $wrapper, $openedItem, opts, cache ) {
//				var openedIdx	= $openedItem.index();
//				
//				$openedItem.find('div.ca-content-wrapper').stop().animate({
//					width	: '0px'
//				}, opts.itemSpeed, opts.itemEasing)
//				.end()
//				.stop()
//				.animate({
//					left	: cache.itemW * ( cache.winpos - 1 ) + 'px'
//				}, opts.itemSpeed, opts.itemEasing, function() {
//					cache.isAnimating	= false;
//					cache.expanded		= false;
//				});
//				
//				// show more link
//				aux.toggleMore( $openedItem, true );
//				
//				$wrapper.find('div.ca-item').each(function(i) {
//					var $item	= $(this),
//						idx		= $item.index();
//					
//					if( idx !== openedIdx ) {
//						$item.find('div.ca-content-wrapper').css({
//							width	: '0px'
//						})
//						.end()
//						.css( 'left', ( ( cache.winpos - 1 ) - ( openedIdx - idx ) ) * cache.itemW + 'px' )
//						.show();
//						
//						// show more link
//						aux.toggleMore( $item, true );
//					}
//				});
//			},
//			// gets the item's position (1, 2, or 3) on the viewport (the visible items)
//			// val is the left of the item
//			getWinPos	: function( val, cache ) {
//				switch( val ) {
//					case 0 					: return 1; break;
//					case cache.itemW 		: return 2; break;
//					case cache.itemW * 2 	: return 3; break;
//				}
//			}
//		}
//                var settings = {
//						sliderSpeed		: 500,			// speed for the sliding animation
//						sliderEasing	: 'easeOutExpo',// easing for the sliding animation
//						itemSpeed		: 500,			// speed for the item animation (open / close)
//						itemEasing		: 'easeOutExpo',// easing for the item animation (open / close)
//						scroll			: 1				// number of items to scroll at a time
//					};
//                var $el 			= $(this),
//                        $wrapper		= $el.find('div.ca-wrapper'),
//                        $items			= $wrapper.children('div.ca-item'),
//                        cache			= {};
//
//                // save the with of one item	
//                cache.itemW			= $items.width();
//                // save the number of total items
//                cache.totalItems	= $items.length;                        
//                element.on('mousedown', function(event) {
//                    $('a.ca-more').bind('click', function( event ) {
//                        if( cache.isAnimating ) return false;
//                        cache.isAnimating	= true;
//                        $(this).hide();
//                        var $item	= $(this).closest('div.ca-item');
//                        aux.openItem( $wrapper, $item, settings, cache );
//                        return false;
//                });
//
//                    // click to close the item(s)
//                    $('a.ca-close').bind('click.contentcarousel', function( event ) {
//                            if( cache.isAnimating ) return false;
//                            cache.isAnimating	= true;
//                            var $item	= $(this).closest('div.ca-item');
//                            aux.closeItems( $wrapper, $item, settings, cache );
//                            return false;
//                    });
//                });
//                
//         }
//         
//    };
//}]);
App.directive('openCloseController', ['$document', function($document) {
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
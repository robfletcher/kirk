(function () {
    'use strict';

    document.addEventListener('DOMContentLoaded', () => {
        setupNavbarToggle();
        setupMenuScrollspy();
    });

    function setupNavbarToggle() {
        // Get all "navbar-burger" elements
        const $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

        // Check if there are any navbar burgers
        if ($navbarBurgers.length > 0) {

            // Add a click event on each of them
            $navbarBurgers.forEach(el => {
                el.addEventListener('click', () => {

                    // Get the target from the "data-target" attribute
                    const target = el.dataset.target;
                    const $target = document.getElementById(target);

                    // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
                    el.classList.toggle('is-active');
                    $target.classList.toggle('is-active');
                });
            });
        }
    }

    function setupMenuScrollspy() {
        // query elements
        var columnMainRight = document.querySelector(".column-main-right");
        if(!columnMainRight) return;

        var content = document.querySelector("article[role=main] .content");
        if(!content) return;

        var menuHeaders = Array.from(columnMainRight.querySelectorAll("article[role=main] .sticky-menu a"));
        var contentHeaders = Array.from(content.querySelectorAll("h1, h2, h3, h4, h5, h6"));

        // zip the menu items to their associated content headers
        var zipped = menuHeaders
            .map(function (menuItem) {
                var href = menuItem.getAttribute("href")
                var contentHeader = contentHeaders.find(contentHeader => ("#" + contentHeader.id) === href);
                var contentHeaderId = contentHeader.id
                return {
                    menuItem: menuItem,
                    menuItemHref: href,
                    contentHeader: contentHeader,
                    contentHeaderId: contentHeaderId,
                    offsetTop: contentHeader.offsetTop
                };
            });

        // don't add the scroll listener if there's no menu to spy
        if (zipped.length === 0) return;

        columnMainRight
            .addEventListener("scroll", function () {
                var scrollPosition = columnMainRight.scrollTop;

                // remove is-active class from all menu items
                zipped.forEach(current => current.menuItem.classList.remove("is-active"));

                // exit early if no content headers are visible
                var visibleMenuItems = zipped.filter(current => scrollPosition >= current.offsetTop);
                if(visibleMenuItems.length === 0) return;

                // add is-active class to last visible menu item
                var lastMenuItem = visibleMenuItems
                    .reduce((prev, current) => (current.offsetTop >= prev.offsetTop) ? current : prev);
                if (lastMenuItem) lastMenuItem.menuItem.classList.add("is-active")
            });
    }
})();

clj-ebay
========

Clojure wrapper library, forked from code by eduardoejp, obtained from Clojars.

This is an small Clojure binding for the Ebay APIs for affiliate marketing.

## Usage

Leiningen 2 has been used for this project.

Edit the "core" file and put your eBay developer APP-ID in place of nil for *app-id*. Then run...


    lein deps

    lein repl


At the REPL...

    (use 'clj-ebay.core)
   
    (use 'clj-ebay.finding)

    ; the following method is a way of handling typos in the search-keywords 
    ; (provided as inputs by end-users, say)

    (get-search-keywords-recommendation "illiam ibson")

    ; the above should return "William Gibson" in the response from eBay


    (find-items-by-keywords {:entries-per-page "9", :global-id "EBAY-US", :page-number 1,  :outputSelector "SellerInfo", :keywords "Neuromancer", :sortOrder "BestMatch" })


    ; get-histograms provides a breakdown of products into various categories
    ; (as a means of narrowing searches).

    (def CATEGORY-ID 377)
    (get-histograms CATEGORY-ID)

The above examples only cover (a fraction of) the functions in the "finding.clj" file. Wrappers for several other eBay APIs have been implemented too.

## License

Distributed under the Eclipse Public License, the same as Clojure.

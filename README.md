
Norman
======

Version 0.1.0.

A warning from King Harold II . . .

*This is pre-release software.*  It may fail to compile.  It might have undocumented features.  It certainly has unimplemented features.  It definitely has bugs.  You have been warned.

Description
-----------

Norman is an XSLT processor (built on top of Saxon) that parallelizes across _n_ processors.

Norman is designed for large transformation tasks.  It is likely to be useful to you only under the following circumstances:

1. You have at least hundreds, if not thousands of texts to process (at [CDRH](http://cdrh.unl.edu/), we have used it process collections exceeding 25,000 files).

2. You can run Norman on multicore hardware.

If those two circumstances obtain, Norman might be just the thing.

Downloading and Running Norman
------------------------------

A pre-compiled jar containing everything needed to run Norman is available here.

You can run it with:

	java -jar norman-X.X.X.jar -s \[your\_stylesheet\]

By default, Norman looks for a `NORMAN\_HOME` environment variable containing a directory path, and will expect to find a directory labeled `input` (where your texts should reside) and `output` (where Norman will write the converted files).  You can set these manually by passing fully qualified paths to the `-i` and `-o` switches.

Norman understands the following switches:

	-s, --stylesheet \[your\_stylesheet\]
	-i, --inputdir \[input directory path\]  
	-o, --outputdir \[Output directory path\] 
	-h, --help
	-V, --version

Building Norman from Source
---------------------------

Abbot is written almost entirely in [Clojure](http://clojure.org/) using the [Leiningen](http://leiningen.org/) build tool.  So assuming you have both Clojure (we're using version 1.4) and the current copy of Leiningen, you should be able to type:

	lein deps
	lein uberjar

That will generate a completely self-contained jar labeled norman-X.X.X-standalone.jar.  You can rename that as you please.

License, etc.
-------------

Written and maintained by Stephen Ramsay for the [Center for Digital Research in the Humanities](http://cdrh.unl.edu/) at the University of Nebraska-Lincoln.

Copyright © 2012 Board of Regents of the University of Nebraska-Lincoln.  Norman may be freely distributed and/or modified subject to certain conditions.  It is made available in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See LICENSE for more details.

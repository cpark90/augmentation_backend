docker run -it --rm --name javatest \
	--network host \
	-v /var/run/docker.sock:/var/run/docker.sock \
	-v /tmp:/tmp \
	-v mosaic:/app/tmp \
	-v mosaic-logs:/app/logs \
	-v ~/git/mosaic_bazel/bundle/src/assembly/resources/etc:/app/etc \
	-v ~/git/mosaic_bazel/bundle/src/assembly/resources/scenarios:/app/scenarios \
    -v ~/git/mosaic/bundle/src/assembly/resources/fed:/app/fed \
	bazel:javatest-image
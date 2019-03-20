1. All source files are under project CLUSTER and package CLUSTER

2. Please run KMeans.java (or KMeans2.java) for results.

3. After running KMeans(or KMeans2), 4 things will be generated:
	a.  a pop-up window showing all the 500 centroid before applying kmeans(or kmeans2).

		In the pop-up window, whited dots means the 10000 points in our toy problem and the red dots
		means the original 500 randomly assigned centroids.

	b.	another pop-up window showing all the 500 controids after applying kmeans(or kmeans 2)

		The second window should pop up immediately after the first  pop-up window for kmeans.
		For kmeans2, please allow 30s for the second window to pop up.

	Please note that, the centroid locations in these plots are rounded. For precise results, please see item c

	c.  KMeans.txt (or KMeans2.txt)

		this is a list of the precise centroid locations after applying kmeans(or kmeans2)

	d. fitnessReport_kmeans.txt (or fitnessReport_kmeans2.txt)

		ShortDistancePercentage (SDP) is the metric I defined to measure the fitness, here is how it works:

	/* No matter applying kmeans or kmeans2, at the very end, every point should belong to a cluster.
	 * Therefore, studying the distribution the 500 centroids should be sufficient to give us clues on how good the fit is
	 *
	 * Ideally, the distance between any two nearby centroids should be no less than 100/sqrt(500) ~ 4.5
	 * To say nearby centroids, two centroids are considered nearby if their distance is less than sqrt(2)* 4.5 ~ 7
	 *
	 * Therefore in general, ideally any distance between tow points that are less than 7 should be greater than 4
	 *
	 * Define shortDistancePercent(SDP) as the percentage that (number of distances less than 4.5) / (number of distance less than 7)
	 * Ideally, SDP should be 0 for a perfect fit, which means the lower SDP, the better the fit*/

	 	fitnessReport_Kmeans.txt (or fitnessReport_kmeans2.txt) shows the SDP before and after fit. We can see that the SDP are cut half after
	 	fit in both cases.


	 

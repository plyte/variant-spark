Variant Spark
==============

_variant-spark_ is a scalable toolkit for genome-wide association studies optimized for GWAS like datasets. 

Machine learning methods and, in particular, random forests (RFs) are a promising alternative to standard single SNP analyses in genome-wide association studies (GWAS). RFs provide variable importance measures to rank SNPs according to their predictive power.
Although there are number of existing random forest implementations available, some even parallel or distributed such as: Random Jungle, ranger or SparkML, most of them are not optimized to deal with GWAS datasets, which usually come with thousands of samples and millions of variables.

_variant-spark_ currently provides the basic functionality of building random forest model and estimating variable importance with mean decrease gini method and can operate on VCF and CSV files. Future extensions will include support of other importance measures, variable selection methods and data formats. 

_variant-spark_ utilizes a novel approach of building random forest from data in transposed representation, which allows it to efficiently deal with even extremely wide GWAS datasets. Moreover, since the most common genomics variant calls VCF and uses the transposed representation, variant-spark can work directly with the VCF data, without the costly pre-processing required by other tools.

_variant-spark_ is built on top of Apache Spark – a modern distributed framework for big data processing, which gives variant-spark the ability to to scale horizontally on both bespoke cluster and public clouds.

The potential users include:

- Medical researchers seeking to perform GWAS-like analysis on large cohort data of genome wide sequencing data or imputed SNP array data.
- Medical researchers or clinicians seeking to perform clustering on genomic profiles to stratify large-cohort genomic data
- General researchers with classification or clustering needs of datasets with millions of features.

### Building

variant-spark requires java jdk 1.8+ and maven 3+

In order to build the binaries use:

	mvn clean install
	
### Running

variant-spark requires an existing spark 2.1+ installation (either a local one or a cluster one).

To run variant-spark use:

	./variant-spark [(--spark|--local) <spark-options>* --] [<command>] <command-options>*

In order to obtain the list of the available commands use:

	./variant-spark -h
	
In order to obtain help for a specific command (for example `importance`) use:

	./variant-spark importance -h

You can use `--spark` marker before the command to pass `spark-submit` options to variant-spark. The list of spark options needs to be terminated with `--`, e.g:

	./variant-spark --spark --master yarn-client --num-executors 32 -- importance .... 
	
Please, note that `--spark` needs to be the first argument of `variant-spark`

You can also run variant-spark in the `--local` mode. In this mode variant-spark will ignore any Hadoop or Spark configuration files and run in the local mode for both Hadoop and Spark. In particular in this mode all file paths are interpreted as local file system paths. Also any parameters passed after `--local` and before `--` are ignored. For example:

	./variant-spark --local -- importance  -if data/chr22_1000.vcf -ff data/chr22-labels.csv -fc 22_16051249 -v -rn 500 -rbs 20 -ro

Note: The difference between running in `--local` mode and in `--spark` with `local` master is that in the latter case Spark uses the hadoop filesystem configuration and the input files need to be copied to this filesystem (e.g. HDFS) 


### Running examples

variant-spark comes with a few example scripts in the `scripts` directory that demonstrate how to run its commands on sample data .

There is a few small data sets in the `data` directory suitable for running in the `--local` mode. The scripts that operate on this data start with the `local-` prefix. For example

	./scripts/local-run-importance-ch22.sh 
	
runs variable importance command on a small sample of the chromosome 22 vcf file (from 1000 Genomes Project)


The full size examples require a cluster environment (the scripts are configured to work with Spark on YARN).

The data required for the examples can be obtained from: [https://bitbucket.csiro.au/projects/PBDAV/repos/variant-spark-data](https://bitbucket.csiro.au/projects/PBDAV/repos/variant-spark-data)

This repository uses the git Large File Support extension, which needs to be installed first (see: [https://git-lfs.github.com/](https://git-lfs.github.com/))

Clone the `variant-spark-data` repository and then to install the test data into your hadoop filesystem use:

	./install-data
	
By default the sample data will installed into the `variant-spark-data\input` sub directory of your HDFS home directory.

You can choose a different location by setting the `VS_DATA_DIR` environment variable.

After the test data has been successfully copied to HDFS you can run examples scripts, e.g.:

	./scripts/run-importance-ch22.sh

Note: if you installed the data to a non default location the `VS_DATA_DIR` needs to be set accordingly when running the examples	

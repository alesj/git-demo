Google Cloud for Java
=====================

[![Build Status](https://travis-ci.org/aozarov/git-demo.svg?branch=master)](https://travis-ci.org/aozarov/git-demo)
[![Coverage Status](https://coveralls.io/repos/aozarov/git-demo/badge.svg)](https://coveralls.io/r/aozarov/git-demo)

Java idiomatic client for Google Cloud Platform services. Supported APIs include:

 * Google Cloud Datastore


> Note: This package is a work-in-progress, and may occasionally
> make backwards-incompatible changes.

Documentation and examples are available [here](https://github.com/GoogleCloudePlatform/gcloud-java/gh-pages/apidocs).

## Google Cloud Datastore

[Google Cloud Datastore][cloud-datastore] ([docs][cloud-datastore-docs]) is a fully
managed, schemaless database for storing non-relational data. Cloud Datastore
automatically scales with your users and supports ACID transactions, high availability
of reads and writes, strong consistency for reads and ancestor queries, and eventual
consistency for all other queries.

Follow the [activation instructions][cloud-datastore-activation] to use the Google
Cloud Datastore API with your project.

    import com.google.gcloud.datastore.DatastoreService;
    import com.google.gcloud.datastore.DatastoreServiceFactory;
    import com.google.gcloud.datastore.DatastoreServiceOptions;
    import com.google.gcloud.datastore.Entity;
    import com.google.gcloud.datastore.Key;
    import com.google.gcloud.datastore.KeyFactory;

    DatastoreServiceOptions options = DatastoreServiceOptions.builder().dataset("...").build();
    DatastoreService datastore = DatastoreServiceFactory.getDefault(options);
    KeyFactory keyFactory = new KeyFactory(datastore).kind("...");
    Key key = keyFactory.newKey(keyName);
    Entity entity = datastore.get(key);
    if (entity == null) {
      entity = Entity.builder(key)
          .set("name", "John Do")
          .set("age", 30)
          .set("updated", false)
          .build();
      datastore.put(entity);
    }

## Contributing

Contributions are welcome. Please, see the
[CONTRIBUTING](https://github.com/GoogleCloudPlatform/gcloud-java/blob/master/CONTRIBUTING.md)
document for details.

[cloud-datastore]: https://cloud.google.com/datastore/
[cloud-datastore-docs]: https://cloud.google.com/datastore/docs
[cloud-datastore-activation]: https://cloud.google.com/datastore/docs/activate

[cloud-pubsub]: https://cloud.google.com/pubsub/
[cloud-pubsub-docs]: https://cloud.google.com/pubsub/docs

[cloud-storage]: https://cloud.google.com/storage/
[cloud-storage-docs]: https://cloud.google.com/storage/docs/overview
[cloud-storage-create-bucket]: https://cloud.google.com/storage/docs/cloud-console#_creatingbuckets

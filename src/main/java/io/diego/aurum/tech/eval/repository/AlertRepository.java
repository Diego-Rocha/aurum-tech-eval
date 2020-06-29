package io.diego.aurum.tech.eval.repository;

import io.diego.aurum.tech.eval.model.entity.Alert;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends DatastoreRepository<Alert, Long> {
}

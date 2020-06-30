package io.diego.aurum.tech.eval.repository;

import io.diego.aurum.tech.eval.model.entity.Appointment;
import org.springframework.cloud.gcp.data.datastore.repository.DatastoreRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends DatastoreRepository<Appointment, Long> {
}

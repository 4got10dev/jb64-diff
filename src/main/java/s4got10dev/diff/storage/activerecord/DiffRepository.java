package s4got10dev.diff.storage.activerecord;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Serhii Homeniuk
 */
interface DiffRepository extends JpaRepository<Diff, Long> {
}

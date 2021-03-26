/*
 * DaftarBelanjaRepo.java
 *
 * Created on Mar 22, 2021, 00.19
 */
package shoppinglist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppinglist.entity.DaftarBelanja;

import java.util.List;


public interface DaftarBelanjaRepo extends JpaRepository<DaftarBelanja, Long> {
    List<DaftarBelanja> findbyTitle(String title);

}

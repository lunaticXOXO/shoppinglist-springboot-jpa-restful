/*
 * ShoppingListService.java
 *
 * Created on Mar 22, 2021, 01.20
 */
package shoppinglist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaRepo;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author irfin
 */
@Service
public class ShoppingListService {
    @Autowired
    private DaftarBelanjaRepo repo;

    public Iterable<DaftarBelanja> getAllData() {
        return repo.findAll();

    }

    public boolean create(DaftarBelanja entity, DaftarBelanjaDetil[] arrDetil){
        try {
            // Pertama simpan dahulu objek DaftarBelanja tanpa mengandung detil apapun.
            repo.save(entity);

            // Setelah berhasil tersimpan, baca primary key auto-generate lalu set sebagai bagian dari
            // ID composite di DaftarBelanjaDetil.
            int noUrut = 1;
            for (DaftarBelanjaDetil detil : arrDetil) {
                detil.setId(entity.getId(), noUrut++);
                entity.addDaftarBarang(detil);
            }
            repo.save(entity);

            return true;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }
    }

    //Mencari daftar belanja berdasarkan judul
    public ResponseEntity<List<DaftarBelanja>> findJudul(@RequestParam String title){
        try{

            List<DaftarBelanja> db = new ArrayList<>();
            repo.findbyTitle(title).forEach(db :: add);
            if(db.isEmpty()){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception ex){
            return  new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Insert Daftar Belanja
    @PostMapping("shoppinglist")
    public ResponseEntity<DaftarBelanja> insertDaftarBelanja(@RequestBody DaftarBelanja db){
        try{
            DaftarBelanja db2 = repo.save(new DaftarBelanja(db.getJudul(),db.getTanggal(),db.getDaftarBarang()));
            return new ResponseEntity<>(db2,HttpStatus.CREATED);
        }catch (Exception ex){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update Daftar Belanja

   public ResponseEntity<DaftarBelanja> updateDaftarBelanja(@PathVariable("id") Long id,@RequestBody DaftarBelanja db){

        Optional<DaftarBelanja> dbData = repo.findById(id);
        if(dbData.isPresent()){
            DaftarBelanja db2 = dbData.get();
            db2.setJudul(db.getJudul());
            db.setTanggal(db.getTanggal());
            return new ResponseEntity<>(repo.save(db2),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
   }

   //Delete Daftar Belanja

    public ResponseEntity<DaftarBelanja> DeleteDaftarBelanja(@PathVariable("id") Long id,@RequestBody DaftarBelanja db){

        try{
            repo.deleteById(id);
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

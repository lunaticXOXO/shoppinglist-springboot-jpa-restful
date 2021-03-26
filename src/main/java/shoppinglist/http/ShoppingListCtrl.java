/*
 * ShoppingListCtrl.java
 *
 * Created on Mar 22, 2021, 01.12
 */
package shoppinglist.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.service.ShoppingListService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author irfin
 */
@RestController
public class ShoppingListCtrl
{
    @Autowired
    private ShoppingListService service;

    /**
     * Mengembalikan daftar objek DaftarBelanja utk pengaksesan HTTP GET.
     *
     * @return
     */
    @GetMapping
    public Iterable<DaftarBelanja> getAll()
    {
        return service.getAllData();
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody ShoppingDataCreateDto json) {
        // Ubah data yg terkandung dlm JSON ke dalam objek yg bisa diterima oleh
        // Service.
        DaftarBelanja entity = new DaftarBelanja();
        entity.setJudul(json.getJudul());

        // Ubah java.util.Date ke LocalDateTime
        LocalDateTime tglLocalDateTime = json.getTanggal().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        entity.setTanggal(tglLocalDateTime);

        List<ShoppingDataCreateDto.DataBarang> listDataBarang = json.getListbarang();
        DaftarBelanjaDetil[] arrDetilBelanja = new DaftarBelanjaDetil[listDataBarang.size()];

        for (int i = 0; i < listDataBarang.size(); i++) {
            arrDetilBelanja[i] = new DaftarBelanjaDetil();
            arrDetilBelanja[i].setByk(listDataBarang.get(i).getByk());
            arrDetilBelanja[i].setMemo(listDataBarang.get(i).getMemo());
            arrDetilBelanja[i].setNamaBarang(listDataBarang.get(i).getNama());
            arrDetilBelanja[i].setSatuan(listDataBarang.get(i).getSatuan());
        }

        if (service.create(entity, arrDetilBelanja))
            return ResponseEntity.ok("Data tersimpan dengan ID: " + entity.getId());
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Data gagal tersimpan");
    }
}

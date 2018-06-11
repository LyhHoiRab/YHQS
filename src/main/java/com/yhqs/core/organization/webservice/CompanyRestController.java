package com.yhqs.core.organization.webservice;

import com.yhqs.core.organization.entity.Company;
import com.yhqs.core.organization.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;
import org.wah.doraemon.security.response.Responsed;

import java.util.List;

@RestController
@RequestMapping(value = "/api/1.0/company")
public class CompanyRestController{

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Company> save(@RequestBody Company company){
        companyService.save(company);

        return new Responsed<Company>("保存成功", company);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Company> update(@RequestBody Company company){
        companyService.update(company);

        return new Responsed<Company>("更新成功", company);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Company> getById(@PathVariable("id") String id){
        Company company = companyService.getById(id);

        return new Responsed<Company>("查询成功", company);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<List<Company>> find(String accountId, String id, String name, String address, String phone){
        List<Company> list = companyService.find(id, name, address, phone, accountId);

        return new Responsed<List<Company>>("查询成功", list);
    }

    @RequestMapping(value = "/find/{accountId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<List<Company>> findByAccountId(@PathVariable("accountId") String accountId, String id, String name,
                                                    String address, String phone){

        List<Company> list = companyService.find(id, name, address, phone, accountId);

        return new Responsed<List<Company>>("查询成功", list);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Page<Company>> page(Long pageNum, Long pageSize, String accountId, String id, String name,
                                         String address, String phone){

        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Page<Company> page = companyService.page(pageRequest, id, name, address, phone, accountId);

        return new Responsed<Page<Company>>("查询成功", page);
    }

    @RequestMapping(value = "/page/{accountId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Page<Company>> pageByAccountId(Long pageNum, Long pageSize, @PathVariable("accountId") String accountId,
                                                    String id, String name, String address, String phone){

        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Page<Company> page = companyService.page(pageRequest, id, name, address, phone, accountId);

        return new Responsed<Page<Company>>("查询成功", page);
    }
}

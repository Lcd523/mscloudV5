package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class OrderController {

    //public static final String PaymentSrv_URL="http://localhost:8001";
    public static final String PaymentSrv_URL="http://cloud-payment-service";//服务注册中心上的微服务名称
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private DiscoveryClient discoveryClient;


    @GetMapping("/pay/add")
    public ResultData addOrder(PayDTO payDTO){
        return restTemplate.postForObject(PaymentSrv_URL+"/pay/add",payDTO,ResultData.class);
    }

    @GetMapping("/pay/del/{id}")
    public void delOrder(@PathVariable("id") Integer id){
        restTemplate.delete(PaymentSrv_URL+"/pay/del/"+id,id);
    }

    @GetMapping("/pay/update")
    public void updateOrder(PayDTO payDTO){
        restTemplate.put(PaymentSrv_URL+"/pay/update",payDTO);
    }

    @GetMapping("/pay/get/{id}")
    public ResultData getOrder(@PathVariable("id") Integer id){
        return restTemplate.getForObject(PaymentSrv_URL+"/pay/get/"+id,ResultData.class);
    }

    @GetMapping("/pay/get/info")
    public String getInfo(){
        return restTemplate.getForObject(PaymentSrv_URL+"/pay/get/info",String.class);
    }

    @GetMapping("/discovery")
    public String discovery()
    {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        System.out.println("===================================");

        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId()+"\t"+element.getHost()+"\t"+element.getPort()+"\t"+element.getUri());
        }
        return instances.get(0).getServiceId()+":"+instances.get(0).getPort();
    }
}

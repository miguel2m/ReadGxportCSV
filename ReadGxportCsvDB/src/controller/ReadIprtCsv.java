/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvConstraintViolationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import model.Iprt;
import readgxportcsvdb.ReadGxportCsvDB;

/**
 *
 * @author P05144
 */
public class ReadIprtCsv {
    /**
     * EN CASO DE QUE EL NETWORK RNC NO SE ENCUENTRE EN LA IPRT DEL NODO
     * @param rnc
     * @return IPRT DEL NODEB
     * @throws IOException
     */
    public static List<Iprt> getIprtNode(String rnc)
            throws IOException{
        
        Path myPath = Paths.get(ReadGxportCsvDB.getDb_dir()+"/IPRT.csv");
        List <Iprt> iprtNodes;
        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<Iprt> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Iprt.class);
            BeanVerifier beanVerifier = (BeanVerifier) (Object t) -> {
                Iprt node  = (Iprt)t;
                return (node.getFilename().contains(rnc)); //To change body of generated lambdas, choose Tools | Templates.
            };
            
            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(Iprt.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withVerifier(beanVerifier)
                    .build();
            
            iprtNodes= csvToBean.parse();
            
            
            
        }
        return iprtNodes;
        
    }
    
    /**
     * EN CASO DE QUE EL NETWORK RNC NO SE ENCUENTRE EN LA IPRT DEL NODO
     * @param _rnc
     * @return IPRT DEL NODEB
     * @throws IOException
     */
    public static List<Iprt> getIprtSN(String _rnc, short _srn)
            throws IOException{
        
        Path myPath = Paths.get(ReadGxportCsvDB.getDb_dir()+"/IPRT.csv");
        List <Iprt> iprtNodes;
        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<Iprt> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Iprt.class);
            BeanVerifier beanVerifier = (BeanVerifier) (Object t) -> {
                Iprt node  = (Iprt)t;
                return (node.getFilename().contains(_rnc) &&
                        node.getSrn() == _srn); //To change body of generated lambdas, choose Tools | Templates.
            };
            
            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(Iprt.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withVerifier(beanVerifier)
                    .build();
            
            iprtNodes= csvToBean.parse();
            

            
        }
        return iprtNodes;
        
    }
    
    /**
     * BUSCA EL NEXTHOP POR SN,SRN Y PUERTO
     * @param _rnc
     * @param _srn
     * @param _sn
     * @return IRP (NEXTHOP)
     * @throws IOException 
     */
    public static List<Iprt> getIprtPortNexthop(String _rnc,short _srn,short _sn)throws IOException{
        
        Path myPath = Paths.get(ReadGxportCsvDB.getDb_dir()+"/IPRT.csv");
        List <Iprt> iprtNodes;
        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<Iprt> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(Iprt.class);
            BeanVerifier beanVerifier = (BeanVerifier) (Object t) -> {
                Iprt node  = (Iprt)t;
                return (node.getFilename().contains(_rnc) &&
                        node.getSrn() == _srn &&
                        node.getSn() == _sn ); //To change body of generated lambdas, choose Tools | Templates.
            };
            
            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(Iprt.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withVerifier(beanVerifier)
                    .build();
            
            iprtNodes= csvToBean.parse();

            
        }
        return iprtNodes;
        
    }
}

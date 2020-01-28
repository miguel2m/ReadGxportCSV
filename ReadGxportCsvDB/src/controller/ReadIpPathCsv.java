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
import model.IpPath;
import readgxportcsvdb.ReadGxportCsvDB;


/**
 *
 * @author Miguelangel
 */
public class ReadIpPathCsv {
    
    /**
     * METODO QUE RETORNA LAS VRF DE LA RNC
     * @param _rnc
     * @return
     * @throws IOException 
     */
    public static List<IpPath> getIpPathNode(String _rnc)throws IOException{
        
        Path myPath = Paths.get(ReadGxportCsvDB.getDb_dir()+"/IPPATH.csv");
        List <IpPath> ipPathNodes;
        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<IpPath> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(IpPath.class);
            BeanVerifier beanVerifier = (BeanVerifier) (Object t) -> {
                IpPath node  = (IpPath)t;
                return (node.getFilename().contains(_rnc)); //To change body of generated lambdas, choose Tools | Templates.
            };
            
            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(IpPath.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withVerifier(beanVerifier)
                    .build();
            
            ipPathNodes= csvToBean.parse();

            
        }
        return ipPathNodes;
        
    }
    

}

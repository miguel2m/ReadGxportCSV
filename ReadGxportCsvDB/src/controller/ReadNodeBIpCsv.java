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
import java.util.ArrayList;
import java.util.List;
import model.NodeBIp;
import readgxportcsvdb.ReadGxportCsvDB;


/**
 *
 * @author Miguelangel
 */
public class ReadNodeBIpCsv {
    public static List<NodeBIp> getNodeBDstip(String _rnc,int node_id){
        
        Path myPath = Paths.get(ReadGxportCsvDB.getDb_dir()+"/NODEBIP.csv");
        List <NodeBIp> nodeB = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(myPath,
                StandardCharsets.UTF_8)) {

            HeaderColumnNameMappingStrategy<NodeBIp> strategy
                    = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(NodeBIp.class);
            BeanVerifier beanVerifier = (BeanVerifier) (Object t) -> {
                NodeBIp node  = (NodeBIp)t;
                return (node.getFilename().contains(_rnc)&&
                        node.getNodebid() == node_id ); //To change body of generated lambdas, choose Tools | Templates.
            };
            
            CsvToBean csvToBean = new CsvToBeanBuilder(br)
                    .withType(NodeBIp.class)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withVerifier(beanVerifier)
                    .build();
            
            nodeB.addAll(csvToBean.parse()) ;
            
            
        }catch(IOException ex){
            System.out.println("Error general "+ex.getMessage());
        }
        return nodeB;
        
    }
}

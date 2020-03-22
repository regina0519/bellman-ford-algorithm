/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abal.bellmanford;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author jimi
 */
public class BellmanFord {
    private List<String> vertices;
    private List<List<Integer>> edges;
    private List<List<List<Integer>>> weights;
    private List<Integer> distances;
    private List<Integer> predecessors;
    
    public BellmanFord(){
        this.vertices=new ArrayList();
        this.edges=new ArrayList();
        this.weights=new ArrayList();
        this.distances=new ArrayList();
        this.predecessors=new ArrayList();
    }
    
    public void addVertex(String vertex){
        if(findVertex(vertex)==-1){
            this.vertices.add(vertex);
            //System.out.println(this.vertices.get(0));
        }else{
            JOptionPane.showMessageDialog(null, "Vertex '"+vertex+"' already exist");
        }
        
    }
    
    public void addEdge(String source, String destination, Integer weight){
        Integer sInd=findVertex(source);
        if(sInd!=-1){
            for(int i=0;i<=sInd-this.edges.size();i++){
                this.edges.add(new ArrayList());
                this.weights.add(new ArrayList());
            }
            Integer dInd=findVertex(destination);
            if(dInd!=-1){
                if(!existedEdge(sInd,dInd)){
                    if(this.edges.get(sInd)==null)this.edges.set(sInd, new ArrayList());
                    this.edges.get(sInd).add(dInd);
                    
                    if(this.weights.get(sInd)==null)this.weights.set(sInd, new ArrayList());
                    List<Integer> tmp=new ArrayList();
                    tmp.add(weight);
                    this.weights.get(sInd).add(tmp);
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Edge '("+source+","+destination+")' already exist");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Destination Vertex '"+destination+"' not found");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Source Vertex '"+source+"' not found");
        }
    }
    
    public Integer findVertex(String vertexToFind){
        Integer ret=-1;
        
        for(int i=0;i<this.vertices.size();i++){
            String tmp=this.vertices.get(i);
            if(tmp.equals(vertexToFind)){
                ret=i;
                break;
            }
        }
        return ret;
    }
    
    private Boolean existedEdge(Integer sInd, Integer dInd){
        Boolean ret=false;
        if(sInd<this.edges.size()){
            for(int ind:this.edges.get(sInd)){
                if(ind==dInd){
                    ret=true;
                    break;
                }
            }
        }
        return ret;
    }
    
    public String displayVertices(){
        String ret="V={";
        for(String tmp:this.vertices){
            if(!ret.equals("V={"))ret+=" , ";
            ret+=tmp;
        }
        ret+="}";
        return ret;
    }
    
    public String displayEdges(){
        String ret="E={";
        for(List<Integer> tmp:this.edges){
            for(Integer tmp2:tmp){
                if(!ret.equals("E={"))ret+=" , ";
                ret+="( "+ this.vertices.get(this.edges.indexOf(tmp)) +" -> "+this.vertices.get(tmp2)+" )";
            }
        }
        ret+="}";
        return ret;
    }
    
    public String displayWeights(){
        String ret="W={";
        for(List<Integer> tmp:this.edges){
            for(Integer tmp2:tmp){
                if(!ret.equals("W={"))ret+=" , ";
                List<Integer> w=this.weights.get(this.edges.indexOf(tmp)).get(tmp.indexOf(tmp2));
                ret+="("+ this.vertices.get(this.edges.indexOf(tmp)) +"->"+this.vertices.get(tmp2)+")="+w.get(0);
            }
        }
        ret+="}";
        return ret;
    }
    
    public String displayDistances(){
        String ret="d={";
        for(Integer tmp:this.distances){
            if(!ret.equals("d={"))ret+=" , ";
            ret+=this.vertices.get(this.distances.indexOf(tmp))+"("+ tmp +")";
        }
        ret+="}";
        return ret;
    }
    
    public String displayPredecessors(){
        String ret="P={";
        for(Integer tmp:this.predecessors){
            if(!ret.equals("P={"))ret+=" , ";
            ret+=this.vertices.get(this.predecessors.indexOf(tmp))+"("+ this.vertices.get(tmp) +")";
        }
        ret+="}";
        return ret;
    }
}

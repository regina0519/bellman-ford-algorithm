/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abal.bellmanford;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author jimi
 */



public class BellmanFord {
    public static final Double MAX_WEIGHT=Double.POSITIVE_INFINITY;
    
    private List<String> vertices;
    private List<List<Integer>> edges;
    private List<List<List<Double>>> weights;
    private List<Double> distances;
    private List<Integer> predecessors;
    
    private List<Integer> resultPath;
    private Double resultCost;
    private MainForm frm;
    
    private Graph graph;
    private List<Node> vVertices;
    private List<List<Edge>> eEdges;
    
    
    public BellmanFord(MainForm frm){
        this.vertices=new ArrayList();
        this.edges=new ArrayList();
        this.weights=new ArrayList();
        this.distances=new ArrayList();
        this.predecessors=new ArrayList();
        this.resultPath=new ArrayList();
        this.resultCost=0.0;
        this.frm=frm;
        this.graph=new SingleGraph("BellmanFord");
        this.graph.addAttribute("ui.stylesheet", this.css());
        this.vVertices=new ArrayList();
        this.eEdges=new ArrayList();
        this.importTemplate();
    }
    
    private String css(){
        String node="node {size:25px;fill-color:#e3cb17,#f5e90c;fill-mode:gradient-horizontal;text-size:14px;}";
        String edge="edge {text-color:#eeeeee;text-background-mode:rounded-box;text-background-color:#000000,#eeeeee;text-padding:5px;text-size:12px;}";
        return node+edge;
    }
    
    private void importTemplate(){
        this.addVertex("A");
        this.addVertex("B");
        this.addVertex("C");
        this.addVertex("D");
        this.addEdge("A", "B", 5.0);
        this.addEdge("A", "C", 2.0);
        this.addEdge("B", "A", 3.0);
        this.addEdge("B", "D", 4.0);
        this.addEdge("C", "D", 6.0);
        this.addEdge("D", "A", -1.0);
    }
    
    public Boolean addVertex(String vertex){
        if(findVertex(vertex)==-1){
            this.vertices.add(vertex);
            this.edges.add(new ArrayList());
            this.weights.add(new ArrayList());
            this.distances.add(BellmanFord.MAX_WEIGHT);
            this.predecessors.add(-1);
            Node n=this.graph.addNode(vertex);
            n.addAttribute("ui.label", vertex);
            this.vVertices.add(n);
            this.eEdges.add(new ArrayList());
            //System.out.println(this.vertices.get(0));
            return true;
        }else{
            JOptionPane.showMessageDialog(null, "Vertex '"+vertex+"' already exist");
            return false;
        }
        
    }
    
    public Boolean addEdge(String source, String destination, Double weight){
        Integer sInd=findVertex(source);
        if(sInd!=-1){
            for(int i=0;i<=sInd-this.edges.size();i++){
                this.edges.add(new ArrayList());
                this.weights.add(new ArrayList());
                this.eEdges.add(new ArrayList());
            }
            Integer dInd=findVertex(destination);
            if(dInd!=-1){
                if(!existedEdge(sInd,dInd)){
                    if(this.edges.get(sInd)==null)this.edges.set(sInd, new ArrayList());
                    this.edges.get(sInd).add(dInd);
                    Edge e=this.graph.addEdge(source+destination, source, destination, true);
                    e.setAttribute("ui.label", weight);
                    this.eEdges.get(sInd).add(e);
                    
                    if(this.weights.get(sInd)==null)this.weights.set(sInd, new ArrayList());
                    List<Double> tmp=new ArrayList();
                    tmp.add(weight);
                    this.weights.get(sInd).add(tmp);
                    return true;
                }else{
                    JOptionPane.showMessageDialog(null, "Edge '("+source+","+destination+")' already exist");
                    return false;
                }
            }else{
                JOptionPane.showMessageDialog(null, "Destination Vertex '"+destination+"' not found");
                return false;
            }
        }else{
            JOptionPane.showMessageDialog(null, "Source Vertex '"+source+"' not found");
            return false;
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
                ret+="( "+ this.vertices.get(this.edges.indexOf(tmp)) +"→"+this.vertices.get(tmp2)+" )";
            }
        }
        ret+="}";
        return ret;
    }
    
    public String displayWeights(){
        String ret="W={";
        for(List<Integer> tmp:this.edges){
            for(int i=0;i<tmp.size();i++){
                Integer tmp2=tmp.get(i);
                if(!ret.equals("W={"))ret+=" , ";
                List<Double> w=this.weights.get(this.edges.indexOf(tmp)).get(i);
                ret+="("+ this.vertices.get(this.edges.indexOf(tmp)) +"→"+this.vertices.get(tmp2)+")="+w.get(0);
            }
        }
        ret+="}";
        return ret;
    }
    
    public String displayDistances(){
        String ret="d={";
        for(int i=0;i<this.distances.size();i++){
            Double tmp=this.distances.get(i);
            if(!ret.equals("d={"))ret+=" , ";
            ret+=this.vertices.get(i)+"("+ tmp +")";
        }
        ret+="}";
        return ret;
    }
    
    public String displayPredecessors(){
        String ret="P={";
        
        for(int i=0;i<this.predecessors.size();i++){
            Integer tmp=this.predecessors.get(i);
            if(!ret.equals("P={"))ret+=" , ";
            if(tmp==-1){
                ret+=this.vertices.get(i)+"(-)";
            }else ret+=this.vertices.get(i)+"("+ this.vertices.get(tmp) +")";
        }
        ret+="}";
        return ret;
    }
    
    public List<String> sourceList(){
        return this.vertices;
    }
    
    public List<String> destinationList(String vertex){
        List<String> ret=new ArrayList();
        Integer i=this.findVertex(vertex);
        if(i==-1)return ret;
        
        for(Integer tmp:this.edges.get(i)){
            ret.add(this.vertices.get(tmp));
        }
        return ret;
    }
    
    public List<Double> weightList(String vertex){
        List<Double> ret=new ArrayList();
        Integer i=this.findVertex(vertex);
        if(i==-1)return ret;
        
        for(List<Double> tmp:this.weights.get(i)){
            ret.add(tmp.get(0));
        }
        return ret;
    }
    
    public List<String> edgeListAll(){
        List<String> ret=new ArrayList();
        for(List<Integer> s:this.edges){
            for(Integer d:s){
                ret.add(this.vertices.get(this.edges.indexOf(s))+"→"+this.vertices.get(d));
            }
        }
        return ret;
    }
    
    public List<Double> weightListAll(){
        List<Double> ret=new ArrayList();
        for(List<List<Double>> s:this.weights){
            for(List<Double> d:s){
                ret.add(d.get(0));
            }
        }
        return ret;
    }
    
    public void initDistances(String startVertex){
        Integer s=this.findVertex(startVertex);
        if(s==-1)return;
        for(Integer i=0;i<this.distances.size();i++){
            if(i==s){
                this.distances.set(i, 0.0);
            }else this.distances.set(i, BellmanFord.MAX_WEIGHT);
            this.predecessors.set(i, -1);
        }
        this.resultPath.clear();
        this.resultCost=0.0;
    }
    
    
    public Graph getGraph(){
        return this.graph;
    }
    
    public List<Double> distanceList(){
        return this.distances;
    }
    
    public List<String> predecessorList(){
        List<String> ret=new ArrayList();
        for(Integer tmp:this.predecessors){
            if(tmp>-1){
                ret.add(this.vertices.get(tmp));
            }else ret.add("-");
        }
        return ret;
    }
    
    public Integer getNumberOfVertices(){
        return this.vertices.size();
    }
    
    public Integer getNumberOfEdges(){
        Integer ret=0;
        for(List<Integer> s:this.edges){
            for(Integer d:s){
                ret++;
            }
        }
        return ret;
    }
    
    public String getResultPath(String finishVertex){
        String ret="";
        Integer ind=this.findVertex(finishVertex);
        if(ind>-1){
            this.resultPath.add(ind);
            
            ret+=finishVertex;
            Integer predInd=this.predecessors.get(ind);
            if(predInd!=-1){
                this.resultCost+=this.countCost(predInd, ind);
                //this.resultPath.add(finishVertex);
                ret=getResultPath(this.vertices.get(predInd))+"→"+ret;
            }
            
        }
        return ret;
    }
    
    private Double countCost(Integer sInd, Integer dInd){
        if(this.resultPath.size()==0)return BellmanFord.MAX_WEIGHT;
        if(sInd<0 || dInd<0)return BellmanFord.MAX_WEIGHT;
        
        List<Integer> s=this.edges.get(sInd);
        List<List<Double>> tmp=this.weights.get(sInd);
        for(Integer i=0;i<s.size();i++){
            Integer d=s.get(i);
            if(d==dInd){
                return tmp.get(i).get(0);
            }
        }
        return BellmanFord.MAX_WEIGHT;
    }
    
    public Double getResultCost(){
        return this.resultCost;
    }
    
    
    //====================================================THE ALGORITHM ===============================
    
    private Double getWeight(Integer u, Integer v){
        Double ret=BellmanFord.MAX_WEIGHT;
        List<List<Double>> s=this.weights.get(u);
        List<Integer> tmp=this.edges.get(u);
        List<Double> d=s.get(tmp.indexOf(v));
        ret=d.get(0);
        return ret;
    }
    
    private void relax(Integer u, Integer v){
        Double w=this.getWeight(u, v);
        Double vd=this.distances.get(v);
        Double ud=this.distances.get(u);
        if(vd>ud+w){
            this.distances.set(v, ud+w);
            this.predecessors.set(v, u);
            //FOR UI
            if(this.frm.showIteration)this.frm.nextRelax(this.vertices.get(u), this.vertices.get(v), w,vd,ud, this.distances.get(v),this.vertices.get(u), true);
        }else{
            //FOR UI
            if(this.frm.showIteration)this.frm.nextRelax(this.vertices.get(u), this.vertices.get(v), w,vd,ud, 0.0,"", false);
        }
    }
    
    private Boolean negativeCycle(Integer u, Integer v){
        Double w=this.getWeight(u, v);
        if(this.distances.get(v)>this.distances.get(u)+w){
            return true;
        }
        return false;
    }
    
    public Boolean doBellmanFord(String startVertex){
        Boolean ret=false;
        this.initDistances(startVertex);//                                      1. Initialize
        
        for(Integer V=0;V<this.getNumberOfVertices()-1;V++){//                  2. Iteration
            if(this.frm.showIteration)this.frm.nextIteration(V+1);
            for(Integer u=0;u<this.edges.size();u++){
                for(Integer vInd=0;vInd<this.edges.get(u).size();vInd++){
                    Integer v=this.edges.get(u).get(vInd);
                    if(this.frm.showIteration)this.frm.nextEdge(this.vertices.get(u), this.vertices.get(v));
                    this.relax(u, v);//                                         3. Relax
                }
            }
        }
        ret=true;
        for(Integer u=0;u<this.edges.size();u++){
            for(Integer vInd=0;vInd<this.edges.get(u).size();vInd++){
                Integer v=this.edges.get(u).get(vInd);
                if(this.negativeCycle(u, v)){
                    ret=false;
                    break;
                }
            }
        }
        
        return ret;
    }
    
    
}

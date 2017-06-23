#!/usr/bin/python
#IMPORT SECTION
#---------------------------
import sys
import os
import numpy as np
import time
import datetime
from scipy.cluster.hierarchy import to_tree
import scipy.cluster.hierarchy as sch
import ete2
from ete2 import Tree,ClusterTree, TreeStyle
from ete2 import  AttrFace, ProfileFace, TextFace
from ete2.treeview.faces import add_face_to_node
import glob
#import scipy.spatial.distance as spd
#from bioservices import UniProt
#u = UniProt()
#END IMPORT SECTION
#-------------------------------------------------------------------
flag=1;array=[] ;SQ_LEN=dict()
clustid=0;sp_name=[]
fname="";pro_name=[]

PRO_pfam=dict();BPMFCC_DICT=dict()
singleton_clust=0;total_protein=0
clust_gt10=0

def getpid(node):
    ms=node.get_leaf_names()
    plist=[]
    PRLIST=[]
    for protein in ms:
        PRLIST.append(protein)
        plist.append(protein.split("_")[0].strip())
    return plist,PRLIST
    
def getGid(GO):
    lista=GO.split(";")
    GOLIST=[]
    if len(lista) > 1:
        for i in range(len(lista)):
            idf=lista[i].split("[")[1].split("]")[0]
            GOLIST.append(idf)
    #else:
    #    GOLIST.append("$")
    GO_STRING=",".join(GOLIST)
    return GO_STRING
    
def loadGO3DIC():
    BPMFCC_DICT=dict()
    sPID=[]
    f=open("./GO_DATA/GO-DATA.tab","r")
    for i,line in enumerate(f):
        if  i>0:
            LN=line.split("\n")[0].split("\t")
            sPID.append(LN[0])
            GO_STR=getGid(LN[2])+","+getGid(LN[3])+","+getGid(LN[4])
            BPMFCC_DICT[LN[0]]=GO_STR
    #print BPMFCC_DICT[LN[0]]
    return BPMFCC_DICT
    

def overlap(seq,strn,lo):
	frq=0
	for i in range(len(seq)-lo):
		if(strn==seq[i:i+lo+1]):
			frq=frq+1
	return frq
#-----------------------------------------------------
def get_reducedfeature(keywords):
    fet_len=4000#00
    f=open("./Weka_result/CorrelationAttributeEval.txt","r")
    for line in f:
        if "Selected attributes:" in line:
            xl=line.split("Selected attributes:")[1].split(":")[0].split(",")[:fet_len]
            attrib_listCR=[int(i.strip()) for i in xl]
            #attrib_listCR=line.split("Selected attributes:")[1].split(":")[0].split(",")[:fet_len]

    f1=open("./Weka_result/InfoGainAttributeEval.txt","r")
    for line in f1:
        if "Selected attributes:" in line:
            xl2=line.split("Selected attributes:")[1].split(":")[0].split(",")[:fet_len]
            attrib_listIG=[int(i.strip()) for i in xl2]
            #attrib_listIG=line.split("Selected attributes:")[1].split(":")[0].split(",")[:fet_len]
    #print attrib_listCR
    Intct=(list(set(attrib_listCR+attrib_listIG)))[:5000]
    Intct.sort()#;print 'Intct',Intct
    reducedFeatures=[]
    for index in Intct:
        nindx=(index)-1
        try:
            reducedFeatures.append(keywords[nindx])
        except:
            print nindx
    reducedFeatures=list(set(reducedFeatures))
    return reducedFeatures 
#-----------------------------------------------------

def dataprocess(fname,lo,PRO_pfam):

    """
    Returns the Data matrix from the Sequence Data
    """
    start_time=time.clock()
    alphabets = ['A', 'C', 'D', 'E', 'F', 'G', 'H','I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'Y']
    keywords=[];global SQ_LEN
    global pro_name;global sp_name
    if(fname=="FOURGRAM"):
        for alpha1 in alphabets:
            for alpha2 in alphabets:
                for alpha3 in alphabets:
                    for alpha4 in alphabets:
                        keywords.append(alpha1+alpha2+alpha3+alpha4)
                    
    if(fname=="TRIGRAM-X"):
        for alpha1 in alphabets:
            for alpha2 in alphabets:
                for alpha3 in alphabets:
                    keywords.append(alpha1+alpha2+alpha3)
    if(fname=="BIGRAM-X"):
        for alpha1 in alphabets:
            for alpha2 in alphabets:
                keywords.append(alpha1+alpha2)
    if(fname=="UNIGRAM-X"):
        for alpha1 in alphabets:
            keywords.append(alpha1)
    
    keywords=get_reducedfeature(keywords)# using reduced feature set
    ROW=20#0#3027#20213    
    COL=len(keywords)
#----------------------------------------------------------------
    print ("New reduced Features = %s"%(COL))
    #----------------------------------------------------------------
    Matrix=np.empty((ROW,COL),dtype=np.int)
    c=0
    Pro_name=[];#sp_name=[]
    fp=open("./SEQUENCE_DATA/Enzyme_Protein.seq","r")
    for line in fp:
        protein=line.split("_")[0]
        sp_name.append(protein)
        P_name=line.split("\t")[0]
        if protein in PRO_pfam:
            pfam_dom=PRO_pfam[protein]
            P_name=  P_name+"_"+pfam_dom
        else:
            P_name=  P_name+"_$*"
        if protein in ENZYME:
            enzy=ENZYME[protein]
            P_name=  P_name+"_"+enzy
        else:
            P_name=  P_name+"_#*"
        Pro_name.append(P_name)   
        seq=line.split("\n")[0].split("\t")[1]
        SQ_LEN[protein]=len(seq)
        sln=protein#sp_name[c]#Pro_name[c]
        #sln=protein   # 0:00:00.011591 seconds
        for i in range(len(keywords)):
              fre=overlap(seq,keywords[i],lo)
              #val=round((float(fre)/float(len(seq)-lo)),4)
              Matrix[c][i]=fre#val
              sln=sln+"\t"+str(fre)#(val)#0:00:00.038400 seconds
              #0:00:04.318760 seconds for int
           #int Total Time Required For Clustering := 0:00:02.818615 seconds
           #float   Total Time Required For Clustering := 0:00:02.917851 seconds
          #float Total Time Required For Clustering := 0:00:06.343372 seconds
        sln=sln+"\n"
        f_l=open("./NWK/%s/heat_%s.txt"%(fname,fname),"a")
        f_l.write(sln)
        f_l.close()
        c=c+1
        if c==ROW:
              break
    print ("Data Matrix Created ...")
    #print ("Data Matrix Normalized ...")  
    print ( "Time Required For DATA Matrix:= %s seconds" % (datetime.timedelta(seconds= (time.clock()-start_time))))
    return Matrix,sp_name#Pro_name

def main():
      start_time1=time.clock()
      #PRO_pfam=dict()
      global PRO_pfam;global fname
      ff=open("./DOMAIN_DATA/uniprot_DOM_20193.tab","r")
      for i,line in enumerate(ff):
          lnm=line.split("\n")[0].split("\t")
          pid=lnm[0]
          #print i
          if lnm[1]!="":
              pfamlist=lnm[1].split(";")[:-1]
              PRO_pfam[pid]="_".join(pfamlist)
          else:
              PRO_pfam[pid]="$"
      BPMFCC_DICT=loadGO3DIC()
      choice=int(raw_input("ENTER YOUR CHOICE\t"))

      if(choice==1):
        fname="UNIGRAM-X"
        opname="uni"
        lo=0
      elif(choice==2):
        fname="BIGRAM-X"
        opname="bi"
        lo=1
      elif(choice==3):
        fname="TRIGRAM-X"
        opname="tri-X"
        lo=2
      elif(choice==4):
        fname="FOURGRAM"
        opname="four"
        lo=3
      elif(choice==5):
        fname="TRIGRAM-WR"
        opname="ZXZ"
        #lo=0
      else:
        print ("WRONG CHIOCE IN 'n' VALUE")
        exit()
      
      dataMatrix,pro_name=dataprocess(fname,lo,PRO_pfam)
      print("-------Data Processing--completed--------")
      
      start_time2=time.clock()
      z=sch.linkage(dataMatrix,method='ward',metric='euclidean')
      #print z
      T=to_tree(z)
     # Tree construction
      root = Tree()
      root.dist = 0
      root.name = "root"
      node_sub = {T: root}
      to_visit = [T]
      while to_visit:
          node = to_visit.pop()
          new_dist = node.dist /2.0
          for clust_node in [node.left, node.right]:
            if clust_node:
                clust = Tree()
                clust.dist = new_dist
                clust.name = str(clust_node.id)
                if clust_node.is_leaf():
                    clust.name=pro_name[int(clust_node.id)]
                node_sub[node].add_child(clust)
                node_sub[clust_node] = clust
                to_visit.append(clust_node)
      tree = root
      
      #tree.write(format=1, outfile="./NWK/"+fname+"/UPROT_HIER_CLUST_"+fname+".nwk")
      tree.write(format=9, outfile="./NWK/"+fname+"/UPROT_HIER_CLUST_"+fname+".nwk")
      #UPROT_HIER_CLUST_TRIGRAM.nwk
      print ( "Time Required For Linkage := %s seconds" % (datetime.timedelta(seconds= (time.clock()-start_time2))))
      print ( "Total Time Required For Clustering := %s seconds" % (datetime.timedelta(seconds= (time.clock()-start_time1))))
      print("-------CLUSTERING--completed--------")
      
      
      TreeStructureProcessing(fname,opname,PRO_pfam,BPMFCC_DICT)
      
      
      
if __name__ == '__main__':
    #PRO_pfam=dict()
    main()
#
'''
-------------------------------
Total clusters = 8
Singleton clusters = 6
Non-Singleton clusters = 2
Total Proteins = 10
---------------completed----------------
Time Required For TreeStructureProcessing:= 0:00:00.070802 seconds

CLUST-1:: Proteins =2:: DCS = 1.0:: inR=0.953913935218:: GCS=0.893
CLUST-2:: Proteins =2:: DCS = 1.0:: inR=0.662622104186:: GCS=0.643
CLUST-3:: Proteins =1:: DCS = 1.0:: inR=999.0:: GCS=1.0
CLUST-4:: Proteins =1:: DCS = 1.0:: inR=999.0:: GCS=1.0
CLUST-5:: Proteins =1:: DCS = 1.0:: inR=999.0:: GCS=1.0
CLUST-6:: Proteins =1:: DCS = 1.0:: inR=999.0:: GCS=1.0
CLUST-7:: Proteins =1:: DCS = 1.0:: inR=999.0:: GCS=1.0
CLUST-8:: Proteins =1:: DCS = 1.0:: inR=999.0:: GCS=1.0

Total clusters = 8
Singleton clusters = 6
Non-Singleton clusters = 2
Total Proteins = 10
---------------completed----------------
Time Required For TreeStructureProcessing:= 0:00:00.084943 seconds

'''


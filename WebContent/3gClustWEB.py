#!/usr/bin/python
#IMPORT SECTION
#---------------------------
import sys,os
from Bio import SeqIO
import numpy as np
import time
import datetime
from scipy.cluster.hierarchy import to_tree
import scipy.cluster.hierarchy as sch
from ete3 import Tree,ClusterTree, TreeStyle
#import multiprocessing
import glob
#import scipy.spatial.distance as spd
#from bioservices import UniProt
#u = UniProt()
#END IMPORT SECTION
#-------------------------------------------------------------------
SEQ_DICT=dict();clustid=0;
singleton_clust=0;total_protein=0
#-------------------------------------------------------------------

def getpidi(node):
    ms=node.get_leaf_names()
    plist=[]
    for protein in ms:
        plist.append(protein)#.split("_")[0])
    return plist

def getpid(node):
    ms=node.get_leaf_names()
    plist=[]
    PRLIST=[];Xprlist=[];Yprlist=[]
    for protein in ms:
        PRT=protein#.split("_")[0].strip()
        Xprlist.append(PRT);Yprlist.append(PRT)
        PRLIST.append(protein)
        plist.append(PRT)
    return plist,PRLIST,Xprlist,Yprlist

def DoCS(PFMlist,PRO_pfam)   :
   ''' Compute Domain Correspondence
       Score for list of protein'''
   DOM_list=[]
   PID_DID=dict()
   for pro in PFMlist:
       pf=PRO_pfam[pro]
       if "$" not in pf:
           if "_" in pf:
               pfl=pf.split("_")
           else:
               pfl=pf.split()
           DOM_list=DOM_list+pfl
           PID_DID[pro]=pfl
       else:
           PFMlist.remove(pro)
   DOM_list= list(set(DOM_list))
   new_matrix=np.zeros((len(PFMlist),len(DOM_list)),dtype=np.double)
   c=0
   for pro1 in PFMlist:
       D_list=PID_DID[pro1]
       for dom in D_list:
           indx=DOM_list.index(dom)
           new_matrix[c][indx]=1
       c+=1
   xsum=0.0
   for r in range(len(PFMlist)):
       xsum+=sum(new_matrix[r,:])/float(len(DOM_list))
   fsum=xsum/float(len(PFMlist))
   return fsum
flag=1
#---------------------
def writeleaf(leavesl,clustid,INTRA,INTER,slhts,jbid):#id_s1,inte_rp,sllhouttep# writeleaf(leavesl,clustid,dcs,gcs,slhts):
    '''
    DCS = Domain Correspondence Score
    GCS = GO Correspondence Score
    SHlS = Sillhoute Score
    '''
    global fname;#global PRO_pfam
    wl="AINTRAS = %s|AINTERS = %s|SHlS = %s\n"%(round(INTRA,4),round(INTER,4),round(slhts,4))
    for leaf in leavesl:
        wl+=leaf+"\n"
    f=open("./OUTPUT/%s/NWK_TO_CLUST/CLUST/CLUST-%s.txt"%(jbid,clustid),"w")
    f.write(wl)
    f.close()
def writeStat(slin,clustid,jbid):
    global fname
    f=open("./OUTPUT/%s/NWK_TO_CLUST/Result_Analysis/Cluster_Statistics_%s.txt"%(jbid,clustid),"a")
    f.write(slin)
    f.close()

#------------------------------------------------------------
def kmer_generate(seq,k):
    #start_time = time.clock()
    ngramlist=[]
    for i in range(len(seq)-k+1):
      xseq=seq[i:i+k]
      ngramlist.append(xseq)
    return ngramlist
#------------------------------------------------------------
def intra_dis(proID):
    global SEQ_DICT;global choice
    choice=5
    sps=0.0;mcv=0;sim_list=[]
    while(len(proID)!=0):
        #newl=[]
        #print proID[0]
        spid=proID[0];xseq=SEQ_DICT[spid]
        if len(xseq)<choice:
            sps+=0
        else:
            kml=kmer_generate(xseq,choice)
            proID.remove(spid)
            for x in proID:
                yseq=SEQ_DICT[x]
                if len(yseq)<choice:
                    sps+=0
                else:
                    jml=kmer_generate(yseq,choice)
                    xdist=float(len([key for key in kml if key in jml]))/(max(len(kml),len(jml)))
                    sps+=xdist;mcv+=1;sim_list.append(xdist)
    #if len(proID)
    sps=float(sps)/max(1,mcv)
    return sps,min(sim_list)

def inter_dis(xproID,yproID):
    global SEQ_DICT;global choice
    choice=5
    sps=0.0;mcv=0;sim_list=[]
    for x in xproID:
        xseq=SEQ_DICT[x]
        if len(xseq)<choice:
            sps+=0
        else:
            kml=kmer_generate(xseq,choice)
            for y in yproID:
                yseq=SEQ_DICT[y]
                if len(yseq)<choice:
                    sps+=0
                else:
                    jml=kmer_generate(yseq,choice)
                    xdist=float(len([key for key in kml if key in jml]))/(max(len(kml),len(jml)))
                    sps+=xdist;mcv+=1;sim_list.append(xdist)

    sps=float(sps)/max(1,mcv)

    return sps,max(sim_list)
    #------------------------------------------------------------------


def Traverse_Cluster_Tree(t,jbid):
    global clustid;global singleton_clust
    global total_protein;global clust_gt10;
    #-------------------------------------------------------------------
    # Calculates some stats on the matrix
    #___________________________________________________________________
    # theta=0.45
    nodep=t.children[0]
    nodeq=t.children[1]
    #------------------------------------
    leaf_names1,Pronm1,A1,B1=getpid(nodep)
    #dcs1 = 0#DoCS(leaf_names1,PRO_pfam)
    #gcs1 = 0#GoCS(leaf_names1,BPMFCC_DICT)
    #print "len(Xpr1)",len(A1)
    #------------------------------------
    leaf_names2,Pronm2,A2,B2=getpid(nodeq)
    #print "len(Xpr2)",len(A2)

    if len(leaf_names1)>300:
        #print "len(A1)",len(A1)
        Traverse_Cluster_Tree(nodep,jbid)
    else:
        #inte_rp,mx1=inter_dis(A1,A2)#between

        if len(A1)>1:
            inte_rp,mx1=inter_dis(A1,A2)#between
            id_s1,mn1=intra_dis(A1)# within inside
        else:
            inte_rp,mx1=inter_dis(A1,A2)#between
            id_s1=1;mn1=0

        if max(id_s1,inte_rp)==0:
            sllhouttep=0.0
        else:
            sllhouttep=float(id_s1-inte_rp)/max(id_s1,inte_rp)


        if (mn1<=mx1  or sllhouttep<0.2)  and len(leaf_names1)>1:# or :
            Traverse_Cluster_Tree(nodep,jbid)
        else:
            clustid+=1
            slin1=('CLUST-%s:: Proteins =%s:: AINTRAS = %s:: AINTERS=%s:: slh=%s\n'%(clustid,len(leaf_names1),round(float(id_s1),4),round(inte_rp,4),round(sllhouttep,4)))
            writeStat(slin1,clustid,jbid)
            writeleaf(Pronm1,clustid,id_s1,inte_rp,sllhouttep,jbid)
            total_protein+=len(leaf_names1)
            if len(leaf_names1)==1:
                singleton_clust+=1
          #  if len(leaf_names1)>=10:
            #    clust_gt10+=1


    if len(leaf_names2)>300:
        #print "len(A2)",len(A1)
        Traverse_Cluster_Tree(nodeq,jbid)
    else:
        inte_rq,mx2=inter_dis(B2,B1)
        if len(B2)>1:
            id_s2,mn2=intra_dis(B2)
        else:
            id_s2=1;mn2=0
        if max(id_s2,inte_rq)==0:
           sllhoutteq=0.0
        else:
            sllhoutteq=float(id_s2-inte_rq)/max(id_s2,inte_rq)

        if (mn2<=mx2 or sllhoutteq<0.2)  and len(leaf_names2)>1:# or :
            Traverse_Cluster_Tree(nodeq,jbid)
        else:

            clustid+=1
            slin2= ('CLUST-%s:: Proteins =%s:: AINTRAS = %s:: AINTERS=%s:: slh=%s\n'%(clustid,len(leaf_names2),round(id_s2,3),round((inte_rq),3),round(sllhoutteq,3)))
            writeStat(slin2,clustid,jbid)
            writeleaf(Pronm2,clustid,id_s2,inte_rq,sllhoutteq,jbid)
            total_protein+=len(leaf_names2)
            if len(leaf_names2)==1:
                singleton_clust+=1
           # if len(leaf_names2)>=10:
             #   clust_gt10+=1
#-------------------------------------------------------------------------------
def showTreeStructure(inputfile):
    t = ClusterTree("./OUTPUT/%s/NWK/HIER_CLUST_%s.nwk"%((inputfile.split("_")[0]).upper(),inputfile.split(".")[0]))


    def mylayout(node):
      global  flag
      #print node.left
     # print "node",len(node )
      #if len(node)!=1:# and flag == 1:
      #    if flag == 1:
      #        pidlist= getpid(node)
              #print pidlist
       #       dcs=DoCS(pidlist,PRO_pfam)
              #print dcs
       #       if dcs>=0.5:
        #          flag=0

             # print pidlist,len(node),dcs
             # print "_____________________"

      if node.is_leaf():
          node.img_style["shape"]="sphere"
          node.img_style["size"]=10
          node.img_style["fgcolor"]='purple'

    t.dist = 0
    t.convert_to_ultrametric(1, strategy="fixed")

    ts = TreeStyle()
    ts.scale =230
    ts.root_opening_factor = 5.0
    ts.mode = "c"
    ts.layout_fn = mylayout
    #print mylayout
    t.show(tree_style=ts)
    print ("--------------------------------")
    print ("completed")


#-----------------------------------------------------
def TreeStructureProcessing(inputfile):
    start_time=time.clock();global array
    global singleton_clust;global total_protein;global clustid
    #files = glob.glob('./NWK_TO_CLUST/'+fname+'/*')
    #for fi in files:
     #   os.remove(fi)


   # fa="./NWK_TO_CLUST/Result_Analysis/Cluster_Statistics_"+fname+".txt"
    #import os.path
   # if os.path.exists(fa):
      #  os.remove(fa)
    t = ClusterTree("./OUTPUT/%s/NWK/HIER_CLUST_%s.nwk"%((inputfile.split("_")[0]).upper(),inputfile.split(".")[0]))
   # t = ClusterTree("./NWK/"+fname+"/UPROT_HIER_CLUST_"+fname+".nwk")#,format=1)
    #array =  t.arraytable

    #-------------------------------------------------------------------
    Traverse_Cluster_Tree(t,(inputfile.split("_")[0]).upper())

    print ("-------------------------------")
    print ("Total clusters = %s"%(clustid))
    print ("Singleton clusters = %s"%(singleton_clust))
    print ("Non-Singleton clusters = %s"%(clustid-singleton_clust))
    print ("Total Proteins = %s"%(total_protein))
    print ("---------------completed----------------")
    print ( "Time Required For Tree Partitionning:= %s seconds" % (datetime.timedelta(time.clock())-datetime.timedelta(start_time)))
#-----------------------------------------------------
def overlap(seq,strn,lo):
	frq=0
	for i in range(len(seq)-lo):
		if(strn==seq[i:i+lo+1]):
			frq=frq+1
	return frq
#-----------------------------------------------------
def get_reducedfeature(keywords):
    f=open("./Weka_result/CorrelationAttributeEval.txt","r")
    for line in f:
        if "Selected attributes:" in line:
            attrib_listCR=line.split("Selected attributes:")[1].split(":")[0].split(",")[:4000]

    f1=open("./Weka_result/InfoGainAttributeEval.txt","r")
    for line in f1:
        if "Selected attributes:" in line:
            attrib_listIG=line.split("Selected attributes:")[1].split(":")[0].split(",")[:4000]

    Intct=(list(set(attrib_listCR+attrib_listIG)))
    reducedFeatures=[]
    for index in Intct:
        nindx=int(index)-1
        reducedFeatures.append(keywords[nindx])

    return reducedFeatures

#-----------------------------------------------------
def dataprocess(fname,lo,inputfile):

    """
    Returns the Data matrix from the Sequence Data
    """
    start_time=time.clock()
    global SEQ_DICT
    alphabets = ['A', 'C', 'D', 'E', 'F', 'G', 'H','I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'Y']
    keywords=[]
    if(fname=="FOURGRAM"):
        for alpha1 in alphabets:
            for alpha2 in alphabets:
                for alpha3 in alphabets:
                    for alpha4 in alphabets:
                        keywords.append(alpha1+alpha2+alpha3+alpha4)

    if(fname=="TRIGRAM"):
        for alpha1 in alphabets:
            for alpha2 in alphabets:
                for alpha3 in alphabets:
                    keywords.append(alpha1+alpha2+alpha3)
    if(fname=="BIGRAM"):
        for alpha1 in alphabets:
            for alpha2 in alphabets:
                keywords.append(alpha1+alpha2)
    if(fname=="UNIGRAM"):
        for alpha1 in alphabets:
            keywords.append(alpha1)

    keywords=get_reducedfeature(keywords)# using reduced feature set

    ROW=0
    COL=len(keywords)
    for record in SeqIO.parse("./INPUT/%s"%(inputfile),"fasta"):
        ROW +=1
    print(ROW)
    #----------------------------------------------------------------
    print ("New reduced Features = %s"%(COL))
    #----------------------------------------------------------------
    Matrix=np.empty((ROW,COL),dtype=np.double)
    c=0
    Pro_name=[]
    for record in SeqIO.parse("./INPUT/%s"%(inputfile),"fasta"):
        ROW +=1
        pidi=(record.id).split("|")[1]
        Pro_name.append(pidi)
        seq=str(record.seq)
        SEQ_DICT[pidi]=seq
        for i in range(len(keywords)):
              fre=overlap(seq,keywords[i],lo)
              val=float(fre)/float(len(seq)-lo)
              Matrix[c][i]=round(val,4)
        c=c+1
        if c==ROW:
              break
    print ("Data Matrix Created ...")
    print ( "Time Required For DATA Matrix:= %s seconds" % (datetime.timedelta(seconds= (time.clock()-start_time))))

    return Matrix,Pro_name


def getNewick(node, newick,leaf_names):
    if node.is_leaf():
        return "%s%s" % (leaf_names[node.id], newick)
    else:
        if len(newick) > 0:
            newick = ")%s" % ( newick)
        else:
            newick = ");"
        newick = getNewick(node.get_left(), newick,  leaf_names)
        newick = getNewick(node.get_right(), ",%s" % (newick),  leaf_names)
        newick = "(%s" % (newick)
        return newick



def main(inputfile):
      start_time1=time.clock()
      choice=3#int(raw_input("ENTER YOUR CHOICE\t"))
      if(choice==1):
        fname="UNIGRAM"
        lo=0
      elif(choice==2):
        fname="BIGRAM"
        lo=1
      elif(choice==3):
        fname="TRIGRAM"
        lo=2
      elif(choice==4):
        fname="FOURGRAM"
        lo=3
      else:
        print ("WRONG CHIOCE IN 'n' VALUE")
        exit()

      dataMatrix,pro_name=dataprocess(fname,lo,inputfile)
      print("-------Data Processing--completed--------")

      start_time2=time.clock()
      z=sch.linkage(dataMatrix,method='ward',metric='euclidean')
      #print z
      tree =to_tree(z,False)
      srt=getNewick(tree, "",  pro_name)
      outfile=open("./OUTPUT/%s/NWK/HIER_CLUST_%s.nwk"%((inputfile.split("_")[0]).upper(),inputfile.split(".")[0]),"w")
      outfile.write(srt);outfile.close()

      #print(srt)

      showTreeStructure(inputfile)
      TreeStructureProcessing(inputfile)




if __name__ == '__main__':
    #print ('Number of arguments:', len(sys.argv), 'arguments.')
    inputfile=str(sys.argv[1])
    #print("inputfile",inputfile)
    main(inputfile)
# python 3gClustWEB.py 2_DATE.fasta
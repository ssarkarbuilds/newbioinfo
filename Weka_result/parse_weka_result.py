# -*- coding: utf-8 -*-
def get_reducedfeature(keywords):
    #f=open("./Weka_result/CorrelationAttributeEval.txt","r")
    f=open("./CorrelationAttributeEval.txt","r")
    for line in f:
        if "Selected attributes:" in line:
            attrib_listCR=line.split("Selected attributes:")[1].split(":")[0].split(",")[:5000]
        #print len(attrib_listCR)

    #f1=open("./Weka_result/InfoGainAttributeEval.txt","r")
    f1=open("./InfoGainAttributeEval.txt","r")
    for line in f1:
        if "Selected attributes:" in line:
            attrib_listIG=line.split("Selected attributes:")[1].split(":")[0].split(",")[:5000]
    #print len(attrib_listIG)

    Intct=(list(set(attrib_listCR+attrib_listIG)))
    #print len(Intct)
    #print type(Intct[0])
    Feature_index=[]
    for index in Intct:
        nindx=int(index)-1
        Feature_index.append(nindx)
    #return Feature_index
    Nkeywords=[]
    for indx in Feature_index:
        Nkeywords.append(keywords[indx])
    #print Nkeywords
    return Nkeywords
alphabets = ['A', 'C', 'D', 'E', 'F', 'G', 'H','I', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'Y']
    
fname="TRIGRAM"        
keywords=[]
if(fname=="TRIGRAM"):
        for alpha1 in alphabets:
            for alpha2 in alphabets:
                for alpha3 in alphabets:
                    keywords.append(alpha1+alpha2+alpha3)   
print (len(get_reducedfeature(keywords)))
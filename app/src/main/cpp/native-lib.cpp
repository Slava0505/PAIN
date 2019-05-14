#include <jni.h>
#include <string>
#include <cmath>
#include <map>
#include <stack>
#include <array>

using namespace std;

struct coop
{
    int x=-1;
    int y=-1;
};

double dist(long x1,long y1,long x2,long y2, long eur)
{
    double eu=sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    double man=abs(x1-x2)+abs(y1-y2);
    if (eur==0)
    {
        return eu;
    }
    else if (eur==1)
    {
        return man;
    }
    else return (eu+man)/2;
}

struct vert
{
    long x=-1;
    long y=-1;
    coop par;
    long wei=0;
    double sum=-1;
    int col=0;
    void upd(int px,int py,int amx,int amy,long pw,int eu,int ex,int ey)
    {
        x=px+amx;
        y=py+amy;
        col=2;
        par.x=px;
        par.y=py;
        wei=pw+1;
        sum=dist(x,y,ex,ey,eu)+pw+1;
    }
};

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_pain_11_AStarActivity_astar(
        JNIEnv *env,
        jobject instance,
        jint w,
        jint h,
        jintArray im1,
        jint bx,
        jint by,
        jint ex,
        jint ey,
        jint eu=0,
        jint dim=0
                ) {
    long m,n;
    coop beg,end,tmp;
    jboolean u;
    int *im=(env)->GetIntArrayElements(im1,&u);
    n=w;
    m=h;
    beg.y=by;
    beg.x=bx;
    end.y=ey;
    end.x=ex;
    vert **a=new vert*[n];
    long i=0;
    long k=0;
    while (i<n)
    {
        a[i]=new vert[m];
        k=0;
        while (k<m)
        {
            int tmp1;
            tmp1=im[i*m+k];
            if (tmp1==1)
            {
                a[i][k].sum=-1;
            }
            a[i][k].col=tmp1;
            k++;
        }
        i++;
    }
    a[end.x][end.y].sum=n*m+1;
    multimap<double,coop>q;
    a[beg.x][beg.y].sum=dist(beg.x,beg.y,end.x,end.y,eu);
    a[beg.x][beg.y].x=beg.x;
    a[beg.x][beg.y].y=beg.y;
    a[beg.x][beg.y].par.x=-1;
    a[beg.x][beg.y].par.y=-1;
    a[beg.x][beg.y].wei=0;
    a[beg.x][beg.y].col=2;
    a[end.x][end.y].col=0;
    tmp.x=beg.x;
    tmp.y=beg.y;
    q.insert(make_pair(a[beg.x][beg.y].sum,tmp));
    coop cur;
    cur = (*q.begin()).second;
    while ((a[end.x][end.y].sum>a[cur.x][cur.y].sum)and(!q.empty()))
    {
        cur=(*q.begin()).second;
        q.erase(q.begin());
        int apx=-1;
        while ((apx<2)and((cur.x!=end.x)or(cur.y!=end.y))) {
            int apy=-1;
            while (apy<2)
            {
               if (((dim!=0)or((dim==0)and((apx==0)or(apy==0))))and((apx!=0)or(apy!=0))){
                    if (((cur.x + apx) < n) and ((cur.x + apx) >= 0) and ((cur.y + apy) < m) and ((cur.y + apy) >= 0) and
                        (a[cur.x + apx][cur.y + apy].col == 0)){
                        a[cur.x+apx][cur.y+apy].upd(cur.x,cur.y,apx,apy,a[cur.x][cur.y].wei,eu,end.x,end.y);
                        tmp.x = cur.x + apx;
                        tmp.y = cur.y + apy;
                        im[(cur.x + apx)*m+cur.y + apy]=2;
                        q.insert(make_pair(a[cur.x + apx][cur.y + apy].sum, tmp));
                    }
                }
                apy++;
            }
            apx++;
        }
    }
    cur.x=end.x;
    cur.y=end.y;
    string res;
    if (a[cur.x][cur.y].par.x==-1)
    {
        res="No way found";
    } else {
        while ((cur.x != beg.x)or(cur.y!=beg.y)) {
            im[cur.x*m+cur.y] = 3;
            cur = a[cur.x][cur.y].par;
        }
        double ln = a[end.x][end.y].sum + 1;
        res="Found a way with length of "+to_string(ceil(ln))+" transitions";
    }
    env->ReleaseIntArrayElements(im1,im,0);
    return env->NewStringUTF(res.c_str());
}

import{s as qe,n as Le,o as Ae,g as Re,r as Ue}from"../chunks/scheduler.Dw9uJjIS.js";import{S as Ne,i as Oe,v as Be,d as ee,f as _,e as i,c as u,g as Pe,x as He,o as L,s as S,h as y,p as P,a as j,b as c,y as Ie,j as R,k as t,m as U,q as x}from"../chunks/index.LMNcmw-L.js";import{u as De}from"../chunks/auth.service.CeFBHbrE.js";function Je(a){let e,r="Loading template...";return{c(){e=i("p"),e.textContent=r},l(l){e=u(l,"P",{"data-svelte-h":!0}),Pe(e)!=="svelte-bmakho"&&(e.textContent=r)},m(l,n){ee(l,e,n)},p:Le,d(l){l&&_(e)}}}function Me(a){let e,r,l=a[4].title+"",n,o,d,I=a[4].description+"",T,V,q,$,F,z,k,B,Q="E-Mail of the signee",X,h,C,te,N,Ce="API Calls",ae,E,O,f,ne,le,H,v,re,oe,D,m,se,ie,J,g,ue,pe,M,b,ce,_e,A,he,G,K=Te(a[2],a[4].id,a[0],a[1],a[3])+"",W,de,fe,ve,Se;return fe=He(a[9][0]),{c(){e=i("div"),r=i("h1"),n=L(l),o=S(),d=i("p"),T=L(I),V=S(),q=i("label"),$=i("input"),F=L(`\r
                Link`),z=S(),k=i("div"),B=i("label"),B.textContent=Q,X=S(),h=i("input"),te=S(),N=i("h2"),N.textContent=Ce,ae=S(),E=i("div"),O=i("label"),f=i("input"),ne=L(" Java"),le=S(),H=i("label"),v=i("input"),re=L(" Curl"),oe=S(),D=i("label"),m=i("input"),se=L(" Python"),ie=S(),J=i("label"),g=i("input"),ue=L(" JavaScript"),pe=S(),M=i("label"),b=i("input"),ce=L(" PHP"),_e=S(),A=i("pre"),he=L("                "),G=i("code"),W=L(K),de=L(`\r
            `),this.h()},l(p){e=u(p,"DIV",{});var s=y(e);r=u(s,"H1",{});var je=y(r);n=P(je,l),je.forEach(_),o=j(s),d=u(s,"P",{});var we=y(d);T=P(we,I),we.forEach(_),V=j(s),q=u(s,"LABEL",{});var me=y(q);$=u(me,"INPUT",{type:!0}),F=P(me,`\r
                Link`),me.forEach(_),z=j(s),k=u(s,"DIV",{class:!0});var Y=y(k);B=u(Y,"LABEL",{for:!0,"data-svelte-h":!0}),Pe(B)!=="svelte-anxqu6"&&(B.textContent=Q),X=j(Y),h=u(Y,"INPUT",{type:!0,id:!0,placeholder:!0,class:!0}),Y.forEach(_),te=j(s),N=u(s,"H2",{"data-svelte-h":!0}),Pe(N)!=="svelte-jz19y9"&&(N.textContent=Ce),ae=j(s),E=u(s,"DIV",{});var w=y(E);O=u(w,"LABEL",{});var ge=y(O);f=u(ge,"INPUT",{type:!0,name:!0}),ne=P(ge," Java"),ge.forEach(_),le=j(w),H=u(w,"LABEL",{});var be=y(H);v=u(be,"INPUT",{type:!0,name:!0}),re=P(be," Curl"),be.forEach(_),oe=j(w),D=u(w,"LABEL",{});var ye=y(D);m=u(ye,"INPUT",{type:!0,name:!0}),se=P(ye," Python"),ye.forEach(_),ie=j(w),J=u(w,"LABEL",{});var Ee=y(J);g=u(Ee,"INPUT",{type:!0,name:!0}),ue=P(Ee," JavaScript"),Ee.forEach(_),pe=j(w),M=u(w,"LABEL",{});var ke=y(M);b=u(ke,"INPUT",{type:!0,name:!0}),ce=P(ke," PHP"),ke.forEach(_),w.forEach(_),_e=j(s),A=u(s,"PRE",{});var Z=y(A);he=P(Z,"                "),G=u(Z,"CODE",{});var $e=y(G);W=P($e,K),$e.forEach(_),de=P(Z,`\r
            `),Z.forEach(_),s.forEach(_),this.h()},h(){c($,"type","checkbox"),c(B,"for","email"),c(h,"type","email"),c(h,"id","email"),c(h,"placeholder","E-Mail"),c(h,"class","input svelte-qcfjii"),h.required=C=!a[0],c(k,"class","svelte-qcfjii"),Ie(k,"visible",!a[0]),c(f,"type","radio"),c(f,"name","language"),f.__value="java",R(f,f.__value),c(v,"type","radio"),c(v,"name","language"),v.__value="curl",R(v,v.__value),c(m,"type","radio"),c(m,"name","language"),m.__value="python",R(m,m.__value),c(g,"type","radio"),c(g,"name","language"),g.__value="javascript",R(g,g.__value),c(b,"type","radio"),c(b,"name","language"),b.__value="php",R(b,b.__value),fe.p(f,v,m,g,b)},m(p,s){ee(p,e,s),t(e,r),t(r,n),t(e,o),t(e,d),t(d,T),t(e,V),t(e,q),t(q,$),$.checked=a[0],t(q,F),t(e,z),t(e,k),t(k,B),t(k,X),t(k,h),R(h,a[1]),t(e,te),t(e,N),t(e,ae),t(e,E),t(E,O),t(O,f),f.checked=f.__value===a[2],t(O,ne),t(E,le),t(E,H),t(H,v),v.checked=v.__value===a[2],t(H,re),t(E,oe),t(E,D),t(D,m),m.checked=m.__value===a[2],t(D,se),t(E,ie),t(E,J),t(J,g),g.checked=g.__value===a[2],t(J,ue),t(E,pe),t(E,M),t(M,b),b.checked=b.__value===a[2],t(M,ce),t(e,_e),t(e,A),t(A,he),t(A,G),t(G,W),t(A,de),ve||(Se=[U($,"change",a[6]),U(h,"input",a[7]),U(f,"change",a[8]),U(v,"change",a[10]),U(m,"change",a[11]),U(g,"change",a[12]),U(b,"change",a[13])],ve=!0)},p(p,s){s&16&&l!==(l=p[4].title+"")&&x(n,l),s&16&&I!==(I=p[4].description+"")&&x(T,I),s&1&&($.checked=p[0]),s&1&&C!==(C=!p[0])&&(h.required=C),s&2&&h.value!==p[1]&&R(h,p[1]),s&1&&Ie(k,"visible",!p[0]),s&4&&(f.checked=f.__value===p[2]),s&4&&(v.checked=v.__value===p[2]),s&4&&(m.checked=m.__value===p[2]),s&4&&(g.checked=g.__value===p[2]),s&4&&(b.checked=b.__value===p[2]),s&31&&K!==(K=Te(p[2],p[4].id,p[0],p[1],p[3])+"")&&x(W,K)},d(p){p&&_(e),fe.r(),ve=!1,Ue(Se)}}}function Ve(a){let e,r;return{c(){e=i("p"),r=L(a[5]),this.h()},l(l){e=u(l,"P",{class:!0});var n=y(e);r=P(n,a[5]),n.forEach(_),this.h()},h(){c(e,"class","error")},m(l,n){ee(l,e,n),t(e,r)},p(l,n){n&32&&x(r,l[5])},d(l){l&&_(e)}}}function Fe(a){let e;function r(o,d){return o[5]?Ve:o[4].title?Me:Je}let l=r(a),n=l(a);return{c(){n.c(),e=Be()},l(o){n.l(o),e=Be()},m(o,d){n.m(o,d),ee(o,e,d)},p(o,[d]){l===(l=r(o))&&n?n.p(o,d):(n.d(1),n=l(o),n&&(n.c(),n.m(e.parentNode,e)))},i:Le,o:Le,d(o){o&&_(e),n.d(o)}}}function Te(a,e,r,l,n){switch(a){case"java":return`
                    import java.io.*;
                    import java.net.*;
                    public class ApiCall {
                        public static void main(String[] args) throws Exception {
                            URL url = new URL("http://localhost:8080/api/templates/use/${e}?link=${r}&signedByEmail=${l}&userId=${n}");
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("POST");
                            con.setRequestProperty("Content-Type", "application/json; utf-8");
                            con.setRequestProperty("Accept", "application/json");
                            con.setDoOutput(true);
                            try (OutputStream os = con.getOutputStream()) {
                                byte[] input = "{}".getBytes("utf-8");
                                os.write(input, 0, input.length);
                            }
                            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                                StringBuilder response = new StringBuilder();
                                String responseLine;
                                while ((responseLine = br.readLine()) != null) {
                                    response.append(responseLine.trim());
                                }
                                System.out.println(response.toString());
                            }
                        }
                    }
                `;case"curl":return`
                    curl -X POST "http://localhost:8080/api/templates/use/${e}?link=${r}&signedByEmail=${l}&userId=${n}" -H "Content-Type: application/json"
                `;case"python":return`
                    import requests

                    url = "http://localhost:8080/api/templates/use/${e}?link=${r}&signedByEmail=${l}&userId=${n}"
                    response = requests.post(url, headers={"Content-Type": "application/json"})
                    print(response.text)
                `;case"javascript":return`
                    fetch("http://localhost:8080/api/templates/use/${e}?link=${r}&signedByEmail=${l}&userId=${n}", {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({})
                    })
                    .then(response => response.json())
                    .then(data => console.log(data));
                `;case"php":return`
                    <?php
                    $url = "http://localhost:8080/api/templates/use/${e}?link=${r}&signedByEmail=${l}&userId=${n}";
                    $options = array(
                        'http' => array(
                            'header'  => "Content-type: application/json\\r\\n",
                            'method'  => 'POST',
                            'content' => '{}',
                        ),
                    );
                    $context  = stream_context_create($options);
                    $result = file_get_contents($url, false, $context);
                    if ($result === FALSE) { /* Handle error */ }
                    var_dump($result);
                    ?>
                `}}function ze(a,e,r){let l=!0,n="",o="java",d,I={},T="";async function V(h){try{const C=await fetch(`http://localhost:8080/api/templates/${h}`);if(!C.ok)throw new Error("Failed to fetch template");r(4,I=await C.json())}catch(C){r(5,T=C.message),console.error("Failed to fetch template:",T)}}Ae(async()=>{if(r(3,d=Re(De)),!d){window.location.href="/login";return}const C=new URLSearchParams(window.location.search).get("id");await V(C)});const q=[[]];function $(){l=this.checked,r(0,l)}function F(){n=this.value,r(1,n)}function z(){o=this.__value,r(2,o)}function k(){o=this.__value,r(2,o)}function B(){o=this.__value,r(2,o)}function Q(){o=this.__value,r(2,o)}function X(){o=this.__value,r(2,o)}return[l,n,o,d,I,T,$,F,z,q,k,B,Q,X]}class Qe extends Ne{constructor(e){super(),Oe(this,e,ze,Fe,qe,{})}}export{Qe as component};

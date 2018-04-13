(function () {
    /* AngularJs Porject Base Name */
    window.BMWApp = "BMWApp";
    /* Base Server Ip address */
    window.ServerIp = "localhost";
    /*  window.ServerIp = "miaorf.vicp.cc" */
    /* Base Server Port Number */
    window.ServerPort = "8080";
    /*  window.ServerPort = "27879" */
    /* Base PorjectName ex:svn Porject Name */
    window.ServerName = "jdc";
    //window.ServerName = "basecode";
    /* Ajax config url */
    window.ServerURL = "http://" + window.ServerIp + ":" + window.ServerPort + "/" + window.ServerName;

    /* Base Server Ip address */
    window.PageIp = "localhost";
    /*  window.PageIp = "miaorf.vicp.cc" */
    /* Base Server Port Number */
    window.PagePort = "63343";
    /*  window.PagePort = "27879" */
    /* Base PorjectName ex:svn Porject Name */
    window.PageName = "idea_auditapp";
    /* Ajax config url */
    window.PageURL = "http://" + window.PageIp + ":" + window.PagePort +"/" + window.PageName;
    /* Base shopID */
    window.shopId = "1";

    window.localAddress = "http://" + window.ServerIp + ":" + window.ServerPort;

    window.formatObj = function(obj){
        for(var p in obj){
            if(obj[p] instanceof Date){
                obj[p] = obj[p].getFullYear() + "-" +(obj[p].getMonth() + 1)+ "-" +obj[p].getDate();
            }
        }
        return obj;
    }

    Array.prototype.distinct = function() {
        var x = [], r = [];
        for(var i = 0; i < this.length; i++) {
            x['_' + this[i]] = this[i];
        }
        for(var b in x) {
            if(typeof x[b] != 'function') {
                r.push(x[b]);
            }
        }
        return r;
    }
}());
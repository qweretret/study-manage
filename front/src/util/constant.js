const constant = {
    //    serverUrl: "http://192.168.1.3:8036/study3",
    //   smsUrl:    "http://192.168.1.3:8036/study3/smsServer",

    //   serverUrl: "http://localhost:8036/study3",
    //   smsUrl:    "http://localhost:8036/study3/smsServer",

    // serverUrl: "http://192.168.1.154:8077/study3stu",
    // smsUrl:    "http://192.168.1.154:8077/study3stu/smsServer",
    
      serverUrl: "http://s3bk1.ossjk.cn/study3",
      smsUrl:    "http://s3bk1.ossjk.cn/study3/smsServer",

    cacheskey: {
        user: "user",
        permissionMenus: "permissionMenus",
        permissionCode: "permissionCode",
        departmentCode: "departmentCode", //部門标识
        departmentCodes: "departmentCodes", //部门标识（包含子部门)
        roleCode: "roleCode", //角色标识
        roleCodes: "roleCodes", //角色标识（包含子角色)
        tabs: "tabs",
        currentTabId: "currentTabId",
        token: "jwttoken",
        theme: "theme",
        menuMode: "menuMode",
        systemSetting: "systemSetting",
        areas: "areas",
        areasStandard: "areasStandard"
    },

    msg: {
        icon: {
            success: 1,
            fail: 2,
            info: 8,
            exception: 5
        }
    },
    response: {
        code: {
            success: 1000,
            fail: 1001,
            exception: 1002,
            nologin: 1003,
            noauth: 1004,
            badtoken: 1005,
            flusntoken: 1006,
        }
    },
    page: {
        pageSizes: [1, 5, 10, 15, 20, 25, 50, 100, 500, 10000],
        layout: "total, sizes, prev, pager, next, jumper",
    },
    responseObj: function() {
        this.code = null;
        this.msg = null;
        this.data = null;
    },
    pageObj: function() {
        this.size = 15;
        this.current = 1;
    }
}
export default constant;
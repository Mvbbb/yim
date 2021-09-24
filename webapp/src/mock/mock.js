const Mock = require('mockjs')
const data = {

        "code": 200,
        "msg": "success",
        "data": {
            "recentChatItems": [
                {
                    "sessionType": "SINGLE",
                    "fromUid": "563178",
                    "toUid": "705273",
                    "groupId": null,
                    "name": "小黑",
                    "avatar": "https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(3).png",
                    "unread": 2,
                    "latestMsgContent": "我是小红，这是一条发给小黑的私聊消息",
                    "msgs": [
                        {
                            "clientMsgId": "1",
                            "serverMsgId": "600990968630427648",
                            "fromUid": "563178",
                            "groupId": null,
                            "sessionType": "SINGLE",
                            "msgType": "TEXT",
                            "msgData": "我是小红，这是一条发给小黑的私聊消息",
                            "timestamp": 1632370223000
                        },
                        {
                            "clientMsgId": "1",
                            "serverMsgId": "600990966285811712",
                            "fromUid": "563178",
                            "groupId": null,
                            "sessionType": "SINGLE",
                            "msgType": "TEXT",
                            "msgData": "我是小红，这是一条发给小黑的私聊消息",
                            "timestamp": 1632370222000
                        }
                    ],
                    "latestMsgDate": 1632370165000
                },
                {
                    "sessionType": "SINGLE",
                    "fromUid": "705273",
                    "toUid": "563178",
                    "groupId": null,
                    "name": "小红",
                    "avatar": "https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(2).png",
                    "unread": 0,
                    "latestMsgContent": "我是小黑，这是一条发给小红的私聊消息",
                    "msgs": null,
                    "latestMsgDate": 1632370199000
                }
            ]
        },
        "timestamp": 1632399026208

}
export default{
    data:data
}


Mock.mock('/api/recent/chat','get',data)

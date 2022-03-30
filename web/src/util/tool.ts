export class Tool {
    /**
     * 空校验null或者''都返回true
     */
    public static isEmpty (obj: any) {
        if((typeof obj === 'string')) {
            return !obj || obj.replace(/\s+/g,"") === "";
        }else {
            return (!obj || JSON.stringify(obj) === "{}" || obj.length === 0);
        }
    }

    /**
     * 非空校验
     */
    public static isNotEmpty(obj: any) {
        return !this.isEmpty(obj);
    }

    /**
     * 对象复制
     */
    public static copy(obj: unknown) {
        if(Tool.isNotEmpty(obj)) {
            return JSON.parse(JSON.stringify(obj));
        }
    }

    /**
     * 随机生成[len]长度的[radix]进制数
     * @param len
     * @param radix 默认62
     * @returns {string}
     */
    public static uuid (len: number, radix = 62) {
        const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        const uuid = [];
        radix = radix || chars.length;

        for (let i = 0; i < len; i++) {
            uuid[i] = chars[0 | Math.random() * radix];
        }

        return uuid.join('');
    }
}

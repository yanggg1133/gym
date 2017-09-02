package com.lvshou.fitnessroom.base.network;

/**
 * API请求参数构造类
 * 目前只支持formData参数构造{@link FormParamsBuilder}
 * json参数构造还没有实现{@link JsonParamsBuilder}
 *
 * Created by je on 9/1/17.
 *
 * @see FormParamsBuilder
 * @see JsonParamsBuilder
 */

public abstract class ParamsBuilder
{
    private ParamsBuilder() {}

    /**
     * 添加参数
     * @param key
     * @param value
     * @return
     */
    public abstract ParamsBuilder putParam(String key,Object value);

    /**
     * 获取拼接后的总参数
     * @return
     */
    abstract String  toParamsStr();


    public static ParamsBuilder buildFormParam()
    {
        return new FormParamsBuilder();
    }


    /**
     * formData 格式参数类实现
     */
    static class FormParamsBuilder extends  ParamsBuilder
    {
        private StringBuilder params = new StringBuilder();
        private static final String paramsSeparator_1 = "&";
        private static final String paramsSeparator_2 = "=";
        @Override
        public ParamsBuilder putParam(String key, Object value)
        {
            params.append(paramsSeparator_1).append(key).append(paramsSeparator_2).append(value);
            return this;
        }

        @Override
        String toParamsStr()
        {
            return params.toString();
        }
    }

    /**
     * json 格式参数类实现
     *
     * <暂未现实>
     */
    static class JsonParamsBuilder extends  ParamsBuilder
    {
        @Override
        public ParamsBuilder putParam(String key, Object value)
        {
            return this;
        }

        @Override
        String toParamsStr()
        {
            return null;
        }
    }

}

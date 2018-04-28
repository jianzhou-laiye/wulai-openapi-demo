package main

import (
	"time"
	//"strconv"
	"fmt"
	"io"
	"crypto/sha1"
	"math/rand"
	"strconv"
)

func GetRandomString(length int64) string {
	str := "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	bytes := []byte(str)
	result := []byte{}
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	for i := int64(0); i < length; i++ {
		result = append(result, bytes[r.Intn(int(len(bytes)))])
	}
	return string(result)
}

func GetHeaders() map[string]string{
	secret := "MY-SECRET"
	pubkey := "MY-KEY"
	nonce := GetRandomString(32)
	timestamp := strconv.Itoa(int(time.Now().Unix()))
	s := fmt.Sprintf("%s%s%s", nonce, timestamp, secret)
	t := sha1.New()
	io.WriteString(t, s)
	sign := fmt.Sprintf("%x", t.Sum(nil))
	return map[string]string{
		"Api-Auth-pubkey": pubkey,
		"Api-Auth-nonce": nonce,
		"Api-Auth-timestamp": timestamp,
		"Api-Auth-sign": sign,
	}
}

func main() {
	fmt.Println(GetHeaders())
}